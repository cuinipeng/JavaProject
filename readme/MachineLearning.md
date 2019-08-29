
### 查询 GPU 信息
----------------
> 下载 [GPU-Z](https://www.techpowerup.com/download/techpowerup-gpu-z/)


### CUDA 和 cuDNN 下载与安装
---------------------
> CUDA是NVIDIA推出的用于自家GPU的并行计算框架, 也就是说CUDA只能在NVIDIA的
  GPU上运行. 在 CUDA 的架构下, 一个程序分为两个部份: host 端和 device 端.
  Host 端是指在 CPU 上执行的部份, 而 device 端则是在显示芯片上执行的部份.
  Device 端的程序又称为 "kernel". 通常 host 端程序会将数据准备好后, 复制
  到显卡的内存中, 再由显示芯片执行 device 端程序, 完成后再由 host 端程序将
  结果从显卡的内存中取回.

> cuDNN(CUDA Deep Neural Network library): 是NVIDIA打造的针对深度神经
  网络的加速库, 是一个用于深层神经网络的GPU加速库. 如果你要用GPU训练模型,
  cuDNN不是必须的, 但是一般会采用这个加速库.

* [GPU，CUDA，cuDNN的理解](https://blog.csdn.net/u014380165/article/details/77340765)
* [CUDA下载 - cuda_10.0.130_411.31_win10.exe](https://developer.nvidia.com/cuda-downloads)
* [cuDNN下载 - cudnn-10.1-windows10-x64-v7.6.3.30.zip](https://developer.nvidia.com/rdp/cudnn-download)

1. 示例程序
   C:\ProgramData\NVIDIA Corporation\CUDA Samples\v10.0
2. 开发工具集
   C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v10.0
3. 配置环境变量
   set CUDA_PATH="C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v10.0"
   set PATH=%CUDA_PATH%\bin;%CUDA_PATH%\lib\x64;%PATH%

   $ nvcc.exe -V
4. 拷贝 cuDNN 到 C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v10.0 对应目录


### 监控 GPU 运行状态
-------------------
```bat
"C:\Program Files\NVIDIA Corporation\NVSMI\nvidia-smi.exe" -l 5
"C:\Program Files\NVIDIA Corporation\NVSMI\nvidia-smi.exe" -q -g 0 -d UTILIZATION -l
```


### tensorflow 使用 GPU 加速
---------------------------
* [TensorFlow使用gpu](https://blog.csdn.net/taolusi/article/details/81096254)
* https://www.tensorfly.cn/tfdoc/how_tos/using_gpu.html
```bat
# 安装 tensorflow
$ pip install tensorflow-gpu
```


### 确认TensorFlow是否已经利用到CUDA
---------------------------------
```python
import os
import tensorflow as tf
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
a = tf.constant([1.0, 2.0, 3.0, 4.0, 5.0, 6.0], shape=[2, 3], name='a')
b = tf.constant([1.0, 2.0, 3.0, 4.0, 5.0, 6.0], shape=[3, 2], name='b')
c = tf.matmul(a, b)
sess = tf.Session(config=tf.ConfigProto(log_device_placement=True))
print(sess.run(c))
```


### 配置Tensorflow
1. 使用指定GPU
   ```python
   CUDA_VISIBLE_DEVICES=1 python example.py
   ```
2. 配置使用显存大小
   ```python
   gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=0.7) # 0.7 倍
   gpu_options = tf.GPUOptions(allow_growth=True) # 按需分配
   sess = tf.Session(config=tf.ConfigProto(gpu_options=gpu_options))
   ```
3. 检查GPU和CPU
    ```python
    import os
    from tensorflow.python.client import device_lib
    os.environ['TF_CPP_MIN_LOG_LEVEL'] = '99'

    print(device_lib.list_local_devices())
    ```
