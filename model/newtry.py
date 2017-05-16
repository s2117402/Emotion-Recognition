from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense
from keras import backend as K
import matplotlib.pyplot as plt
import h5py
# import PIL
# import scikit-image
# dimensions of our images.
img_width, img_height = 150, 150;    #input image is 150*150

train_data_dir = '/train'            #trainning picture
validation_data_dir = '/validation'   #validation picture
nb_train_samples = 2348;               #trainning data number
nb_validation_samples = 180            #validation data
epochs = 50
batch_size = 16



if K.image_data_format() == 'channels_first':         #avoid the format problem
    input_shape = (3, img_width, img_height)
else:
    input_shape = (img_width, img_height, 3)

# layer1
model = Sequential()
model.add(Conv2D(32, (3, 3), input_shape=input_shape))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

#layer 2
model.add(Conv2D(32, (3, 3)))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

# layer3
model.add(Conv2D(64, (3, 3)))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

# layer4
model.add(Flatten())
model.add(Dense(1024))

#layer4
model.add(Activation('relu'))
model.add(Dropout(0.25))
model.add(Dense(3))
model.add(Activation('softmax'))

# compile
model.compile(loss='categorical_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])

# this is the augmentation configuration we will use for training
train_datagen = ImageDataGenerator(
    rescale=1. / 255,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True)

# this is the augmentation configuration we will use for testing:
# only rescaling
test_datagen = ImageDataGenerator(rescale=1. / 255)

train_generator = train_datagen.flow_from_directory(
    train_data_dir,
    target_size=(img_width, img_height),
    batch_size=batch_size,
    class_mode='categorical')

validation_generator = test_datagen.flow_from_directory(
    validation_data_dir,
    target_size=(img_width, img_height),
    batch_size=batch_size,
    class_mode='categorical')



#generate model
result=model.fit_generator(
    train_generator,
    steps_per_epoch=nb_train_samples / batch_size,
    epochs=epochs,
    validation_data=validation_generator,
    validation_steps=nb_validation_samples / batch_size)


##################################################
# plot the result
##################################################
# to see the performance of algorithm
plt.figure
plt.plot(result.epoch,result.history['acc'],label="acc")
plt.plot(result.epoch,result.history['val_acc'],label="val_acc")
plt.scatter(result.epoch,result.history['acc'],marker='*')
plt.scatter(result.epoch,result.history['val_acc'])
plt.show()
model.save('my_model.h5')                 #save the model
####################################
# predict part
###################################
result=model.predict_classes('test.jpg',batch_size=32,verbose=1)  #use model to do a prediction



