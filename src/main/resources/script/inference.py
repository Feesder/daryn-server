import pathlib

import PIL
import os
import numpy as np
import tensorflow as tf
import socketio
import requests

from keras import layers
from keras.models import Sequential

sio = socketio.Server(cors_allowed_origins='*')
app = socketio.WSGIApp(sio)

dataset_dir = pathlib.Path("datasets")
batch_size = 32
img_width = 180
img_height = 180

train_ds = tf.keras.utils.image_dataset_from_directory(
    dataset_dir,
    validation_split=0.2,
    subset="training",
    seed=123,
    image_size=(img_height, img_width),
    batch_size=batch_size
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    dataset_dir,
    validation_split=0.2,
    subset="validation",
    seed=123,
    image_size=(img_height, img_width),
    batch_size=batch_size
)

class_names = train_ds.class_names

AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.cache().shuffle(1000).prefetch(buffer_size=AUTOTUNE)
val_ds = val_ds.cache().prefetch(buffer_size=AUTOTUNE)

num_classes = len(class_names)
model = Sequential(
    [
        layers.experimental.preprocessing.Rescaling(1. / 255, input_shape=(img_height, img_width, 3)),
        layers.experimental.preprocessing.RandomFlip("horizontal", input_shape=(img_height, img_width, 3)),
        layers.experimental.preprocessing.RandomRotation(0.1),
        layers.experimental.preprocessing.RandomZoom(0.1),
        layers.experimental.preprocessing.RandomContrast(0.2),
        layers.Conv2D(16, 3, padding="same", activation="relu"),
        layers.MaxPooling2D(),
        layers.Conv2D(32, 3, padding="same", activation="relu"),
        layers.MaxPooling2D(),
        layers.Conv2D(64, 3, padding="same", activation="relu"),
        layers.MaxPooling2D(),
        layers.Dropout(0.2),
        layers.Flatten(),
        layers.Dense(128, activation="relu")
    ]
)

model.compile(
    optimizer="adam",
    loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
    metrics=["accuracy"]
)

model.load_weights("roads_model").expect_partial()

loss, acc = model.evaluate(train_ds, verbose=2)


@sio.on('image')
def handle_image(sid, data):
    img = tf.keras.utils.load_img(f"{data}.jpg", target_size=(img_height, img_width))
    img_array = tf.keras.utils.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)

    predictions = model.predict(img_array)
    score = tf.nn.softmax(predictions[0])
    print("На изображении скорее всего {} ({:.2f}% вероятность)".format(class_names[np.argmax(score)],
                                                                        100 * np.max(score)))
    requests.put('http://localhost:8080/api/v1/mark', class_names[np.argmax(score)], params={"id": data})
    os.remove(f"{data}.jpg")


if __name__ == '__main__':
    import eventlet.wsgi

    eventlet.wsgi.server(eventlet.listen(('localhost', 8082)), app)
