import tensorflow as tf
import numpy as np
import pandas as pd
import math


raw_data = pd.read_excel("TDdata.xlsx", header = None)
raw_data = raw_data.as_matrix()
y_value = np.nan_to_num(np.array(raw_data[:,4]).reshape(-1,1))
x_value = np.nan_to_num(np.array(raw_data[:,0:4]))
x_mean = x_value.mean(axis = 0)
x_std = x_value.std(axis = 0)
x_value = (x_value - x_mean) / x_std
#print(x_value)

def add_layer(inputs, in_size, out_size, activation_function = None):
    Weights = tf.Variable(tf.random_normal([in_size, out_size]))
    biases = tf.Variable(tf.zeros([1,out_size]) + 0.1)
    Z = tf.matmul(inputs, Weights) + biases
    if activation_function is None:
        outputs = Z
    else:
        outputs = activation_function(Z)
    return outputs


xs = tf.placeholder(tf.float32, [None, 4])
ys = tf.placeholder(tf.int32, [None, 1])

# 4 - features, 10 - num of hidden units
l1 = add_layer(xs, 4, 10, activation_function=tf.nn.relu)
# 10 - 1st hidden layer, 10 - 2nd hidden layer
l2 = add_layer(l1, 10, 10, activation_function=tf.nn.relu)
# 10 - 2nd hidden layer, 10 - 3rd hidden layer
l3 = add_layer(l2, 10, 10, activation_function=tf.nn.relu)
# 10 - 3rd hidden layer, 3 - num of classes
prediction = add_layer(l3, 10, 3, activation_function=None)

y_value = y_value.astype("int32")
#print(y_value)
loss = tf.losses.sparse_softmax_cross_entropy(labels=ys, logits=prediction)
train_step = tf.train.GradientDescentOptimizer(0.2).minimize(loss)

init = tf.initialize_all_variables()
sess = tf.Session()
sess.run(init)

assert(x_value.shape[0] == y_value.shape[0])
m = x_value.shape[0]
ms = math.floor(m / 10)
for n in range(10):
    print("Cross Validation : " + str(n+1) + " ====================================================")
    x_test = x_value[n*ms:(n+1)*ms+1, :]
    y_test = y_value[n*ms:(n+1)*ms+1, :]
    x_train = np.vstack((x_value[0:n*ms, :], x_value[(n+1)*ms+1:, :]))
    y_train = np.vstack((y_value[0:n*ms, :], y_value[(n+1)*ms+1:, :]))

    for i in range(3000):
        sess.run(train_step, feed_dict={xs: x_train, ys: y_train})
        #if i%100 == 0:
        #    print(sess.run(loss, feed_dict={xs: x_train, ys: y_train}))

    pred = sess.run(prediction, feed_dict={xs: x_train, ys: y_train})
    pred = np.array(pred)

    max_pred = np.array(pred.argmax(axis = 1)).reshape(-1,1)
    decline = 0
    decline_true = 0
    rise = 0
    rise_true = 0
    maintain = 0
    maintain_true = 0
    for i in range(y_train.shape[0]):
        if y_train[i, 0] == 0:
            decline += 1
            if max_pred[i, 0] == 0:
                decline_true += 1
        elif y_train[i, 0] == 1:
            maintain += 1
            if max_pred[i, 0] == 1:
                maintain_true += 1
        else:
            rise += 1
            if max_pred[i, 0] == 2:
                rise_true += 1
    print("Training Result -----------------------------------")
    print("decrease acc = " + str(decline_true / decline))
    print("maintain acc = " + str(maintain_true / maintain))
    print("increase acc = " + str(rise_true / rise))

    tp = 0
    tn = 0
    fn = 0
    fp = 0
    for i in range(y_train.shape[0]):
        if max_pred[i, 0] == 1:
            if y_train[i, 0] == 1:
                tp += 1
            else:
                fp += 1
        else:
            if y_train[i, 0] == 1:
                fn += 1
            else:
                tn += 1
    print("For maintain:")
    print("TP = " + str(tp) + " TN = " + str(tn) + " FN = " + str(fn) + " FP = " + str(fp))
    print("precision = " + str(tp/(tp+fp)) + "  recall = " + str(tp/(tp+fn)))

    pred = sess.run(prediction, feed_dict={xs: x_test, ys: y_test})
    pred = np.array(pred)

    max_pred = np.array(pred.argmax(axis=1)).reshape(-1, 1)
    decline = 0
    decline_true = 0
    rise = 0
    rise_true = 0
    maintain = 0
    maintain_true = 0
    for i in range(y_test.shape[0]):
        if y_test[i, 0] == 0:
            decline += 1
            if max_pred[i, 0] == 0:
                decline_true += 1
        elif y_test[i, 0] == 1:
            maintain += 1
            if max_pred[i, 0] == 1:
                maintain_true += 1
        else:
            rise += 1
            if max_pred[i, 0] == 2:
                rise_true += 1
    print("Test Result ---------------------------------------")
    print("decrease acc = " + str(decline_true / decline))
    print("maintain acc = " + str(maintain_true / maintain))
    print("increase acc = " + str(rise_true / rise))

    tp = 0
    tn = 0
    fn = 0
    fp = 0
    for i in range(y_test.shape[0]):
        if max_pred[i, 0] == 1:
            if y_test[i, 0] == 1:
                tp += 1
            else:
                fp += 1
        else:
            if y_test[i, 0] == 1:
                fn += 1
            else:
                tn += 1
    print("For maintain:")
    print("TP = " + str(tp) + " TN = " + str(tn) + " FN = " + str(fn) + " FP = " + str(fp))
    print("precision = " + str(tp / (tp + fp)) + "  recall = " + str(tp / (tp + fn)))