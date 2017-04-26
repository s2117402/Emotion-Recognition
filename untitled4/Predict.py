from PIL import Image
import numpy as np
import keras
import imgRc
import PR
def predict():
    data = np.empty((1, 100, 100, 1), dtype="float32")
    img = Image.open("static/uploads/image01.jpg")
    arr = np.asarray(img, dtype="float32")
    data[0, :, :, :] = arr.reshape(100, 100, 1)
    model = keras.models.load_model('model/my_model.h5')
    result=model.predict_classes(data,batch_size=32,verbose=1)
    return result[0]
