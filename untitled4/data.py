# coding:utf-8
"""
Author:wepon
Source:https://github.com/wepe

"""

import os
from PIL import Image
import numpy as np
from keras import backend as K



# 读取文件夹mnist下的42000张图片，图片为灰度图，所以为1通道，图像大小28*28
# 如果是将彩色图作为输入,则将1替换为3，并且data[i,:,:,:] = arr改为data[i,:,:,:] = [arr[:,:,0],arr[:,:,1],arr[:,:,2]]
def load_data():
    data = np.empty((2393,100, 100,1), dtype="float32")
    label = np.empty((2393,), dtype="uint8")

    imgs = os.listdir("data/data/")
    num = len(imgs)
    for i in range(num):
        img = Image.open("data/data/" + imgs[i])
        arr = np.asarray(img, dtype="float32")
        data[i, :, :, :] = arr.reshape(100,100,1)
        label[i] = int(imgs[i].split('.')[0])

    return data, label


def read_pic():
    data = np.empty((1,100, 100,1), dtype="float32")
    img=Image.open("data/test/2.1251.jpg")
    arr = np.asarray(img, dtype="float32")
    data[0,:,:,:]=arr.reshape(100,100,1)
    return data

