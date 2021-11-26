**Convert MP4 Service** 

**视频转换MP4服务--测试说明**

# 测试方法

本项目已经集成了SwaggerUI，可以直接访问：http://127.0.0.1:8080/swagger-ui/

在对应接口中输入需要传入的JSON，即可执行测试。

# 输入：path方式

## 输出：Path方式

输入path方式，输出Path方式，即从本地指定位置读取源视频文件，转换后将文件直接写入到指定的目标文件夹，且文件名按照传入的文件名命名。

此种方式适合采用NAS存储附件的方式，可以将公用的NAS挂接到本服务器中，转换后的文件按照业务系统的文件名规范生成新的MP4文件，并存储在指定位置。业务系统可以直接读取转换后的MP4文件，进行在线播放。

传入的JSON示例如下：

```json
{
	"inputType": "path",
	"inputFile": "D:/001.MOV",
	"mp4FileName": "001-online",
	"writeBackType": "path",
	"writeBack": {
		"path": "D:/cvtest/"
	}
}
```

转换执行时，系统控制台输出如下内容：

```
14:23:59.902 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 直接转成mp4格式
ffmpeg (Format Factory Revision)version 4.2.1 Copyright (c) 2000-2019 the FFmpeg developers
  built with gcc 8.3.0 (Rev2, Built by MSYS2 project)
  configuration: --prefix=/mingw64 --arch=x86_64 --disable-static --enable-shared --enable-small --disable-debug --extra-cflags='-ID:\CodeLib\Video_Codec_SDK_10.0.26\include -O2' --extra-ldflags='-LD:\CodeLib\Video_Codec_SDK_10.0.26\Lib\x64' --enable-gpl --enable-version3 --enable-nonfree --enable-cuda --enable-cuvid --enable-nvenc --enable-amf --enable-libmfx --enable-encoder=h264_qsv --enable-decoder=h264_qsv --enable-encoder=hevc_qsv --enable-avresample --enable-libass --enable-libzvbi --enable-fontconfig --enable-iconv --enable-libbs2b --enable-libfreetype --enable-libgsm --enable-libilbc --enable-libmp3lame --enable-libopencore-amrnb --enable-libopencore-amrwb --enable-libopenjpeg --enable-libopus --enable-libspeex --enable-libtheora --enable-libtwolame --enable-libvidstab --enable-libvo-amrwbenc --enable-libvorbis --enable-libvpx --enable-libwebp --enable-libwavpack --enable-libx264 --enable-libx265 --enable-libxvid --enable-libaom --enable-bzlib --enable-lzma --enable-zlib --extra-libs='-static-libgcc -static-libstdc++ -lstdc++ -lgcc_eh -lpthread -lintl -liconv'
  libavutil      56. 31.100 / 56. 31.100
  libavcodec     58. 54.100 / 58. 54.100
  libavformat    58. 29.100 / 58. 29.100
  libavdevice    58.  8.100 / 58.  8.100
  libavfilter     7. 57.100 /  7. 57.100
  libavresample   4.  0.  0 /  4.  0.  0
  libswscale      5.  5.100 /  5.  5.100
  libswresample   3.  5.100 /  3.  5.100
  libpostproc    55.  5.100 / 55.  5.100
[mov,mp4,m4a,3gp,3g2,mj2 @ 000001eee5a49600] st: 0 edit list: 1 Missing key frame while searching for timestamp: 1001
[mov,mp4,m4a,3gp,3g2,mj2 @ 000001eee5a49600] st: 0 edit list 1 Cannot find an index entry before timestamp: 1001.
Guessed Channel Layout for Input Stream #0.1 : stereo
Input #0, mov,mp4,m4a,3gp,3g2,mj2, from 'D:/001.MOV':
  Metadata:
    major_brand     : qt  
    minor_version   : 537331968
    compatible_brands: qt  niko
    creation_time   : 2020-08-30T21:17:40.000000Z
  Duration: 00:00:58.26, start: 0.000000, bitrate: 22238 kb/s
    Stream #0:0(eng): Video: h264 (avc1 / 0x31637661), yuvj420p(pc, smpte170m/bt709/bt470m), 1920x1080 [SAR 1:1 DAR 16:9], 20663 kb/s, 29.97 fps, 29.97 tbr, 30k tbn, 59.94 tbc (default)
    Metadata:
      creation_time   : 2020-08-30T21:17:40.000000Z
    Stream #0:1(eng): Audio: pcm_s16le (sowt / 0x74776F73), 48000 Hz, stereo, s16, 1536 kb/s (default)
    Metadata:
      creation_time   : 2020-08-30T21:17:40.000000Z
Stream mapping:
  Stream #0:0 -> #0:0 (h264 (native) -> h264 (libx264))
  Stream #0:1 -> #0:1 (pcm_s16le (native) -> aac (native))
Press [q] to stop, [?] for help
[swscaler @ 000001eee5f28700] deprecated pixel format used, make sure you did set range correctly
[libx264 @ 000001eee5b0eb40] using SAR=1/1
[libx264 @ 000001eee5b0eb40] using cpu capabilities: MMX2 SSE2Fast SSSE3 SSE4.2 AVX FMA3 BMI2 AVX2
[libx264 @ 000001eee5b0eb40] profile High, level 4.0, 4:2:0, 8-bit
[libx264 @ 000001eee5b0eb40] 264 - core 157 - H.264/MPEG-4 AVC codec - Copyleft 2003-2018 - http://www.videolan.org/x264.html - options: cabac=1 ref=3 deblock=1:0:0 analyse=0x3:0x113 me=hex subme=7 psy=1 psy_rd=1.00:0.00 mixed_ref=1 me_range=16 chroma_me=1 trellis=1 8x8dct=1 cqm=0 deadzone=21,11 fast_pskip=1 chroma_qp_offset=-2 threads=12 lookahead_threads=2 sliced_threads=0 nr=0 decimate=1 interlaced=0 bluray_compat=0 constrained_intra=0 bframes=3 b_pyramid=2 b_adapt=1 b_bias=0 direct=1 weightb=1 open_gop=0 weightp=2 keyint=250 keyint_min=25 scenecut=40 intra_refresh=0 rc_lookahead=40 rc=crf mbtree=1 crf=23.0 qcomp=0.60 qpmin=0 qpmax=69 qpstep=4 ip_ratio=1.40 aq=1:1.00
Output #0, mp4, to 'D:/cvtest/001-online.mp4':
  Metadata:
    major_brand     : qt  
    minor_version   : 537331968
    compatible_brands: qt  niko
    encoder         : Lavf58.29.100
    Stream #0:0(eng): Video: h264 (libx264) (avc1 / 0x31637661), yuv420p(progressive), 1920x1080 [SAR 1:1 DAR 16:9], q=-1--1, 29.97 fps, 30k tbn, 29.97 tbc (default)
    Metadata:
      creation_time   : 2020-08-30T21:17:40.000000Z
      encoder         : Lavc58.54.100 libx264
    Side data:
      cpb: bitrate max/min/avg: 0/0/0 buffer size: 0 vbv_delay: -1
    Stream #0:1(eng): Audio: aac (mp4a / 0x6134706D), 48000 Hz, stereo, fltp, 128 kb/s (default)
    Metadata:
      creation_time   : 2020-08-30T21:17:40.000000Z
      encoder         : Lavc58.54.100 aac
[mp4 @ 000001eee6551740] Starting second pass: moving the moov atom to the beginning of the file
frame= 1746 fps= 18 q=-1.0 Lsize=   50234kB time=00:00:58.26 bitrate=7063.3kbits/s speed=0.615x    
video:49257kB audio:913kB subtitle:0kB other streams:0kB global headers:0kB muxing overhead: 0.125752%
[libx264 @ 000001eee5b0eb40] frame I:15    Avg QP:22.94  size: 69122
[libx264 @ 000001eee5b0eb40] frame P:474   Avg QP:25.65  size: 45787
[libx264 @ 000001eee5b0eb40] frame B:1257  Avg QP:26.95  size: 22036
[libx264 @ 000001eee5b0eb40] consecutive B-frames:  1.5%  1.4% 18.0% 79.0%
[libx264 @ 000001eee5b0eb40] mb I  I16..4: 21.7% 71.5%  6.8%
[libx264 @ 000001eee5b0eb40] mb P  I16..4: 13.1% 27.1%  1.8%  P16..4: 40.3%  9.8%  2.8%  0.0%  0.0%    skip: 5.1%
[libx264 @ 000001eee5b0eb40] mb B  I16..4:  3.2%  4.1%  0.1%  B16..8: 45.4%  7.9%  0.8%  direct: 6.0%  skip:32.6%  L0:47.5% L1:47.6% BI: 4.9%
[libx264 @ 000001eee5b0eb40] 8x8 transform intra:62.2% inter:86.5%
[libx264 @ 000001eee5b0eb40] coded y,uvDC,uvAC intra: 43.4% 67.6% 22.4% inter: 22.7% 33.4% 0.9%
[libx264 @ 000001eee5b0eb40] i16 v,h,dc,p: 11% 44%  6% 39%
[libx264 @ 000001eee5b0eb40] i8 v,h,dc,ddl,ddr,vr,hd,vl,hu: 16% 37% 18%  3%  4%  4%  7%  3%  7%
[libx264 @ 000001eee5b0eb40] i4 v,h,dc,ddl,ddr,vr,hd,vl,hu: 17% 42% 12%  3%  5%  5%  7%  3%  5%
[libx264 @ 000001eee5b0eb40] i8c dc,h,v,p: 45% 33% 15%  8%
[libx264 @ 000001eee5b0eb40] Weighted P-Frames: Y:1.1% UV:0.6%
[libx264 @ 000001eee5b0eb40] ref P L0: 52.5% 15.6% 24.0%  7.9%  0.1%
[libx264 @ 000001eee5b0eb40] ref B L0: 76.1% 19.3%  4.6%
[libx264 @ 000001eee5b0eb40] ref B L1: 88.8% 11.2%
[libx264 @ 000001eee5b0eb40] kb/s:6926.27
[aac @ 000001eee6bd9080] Qavg: 251.189
14:25:34.916 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - ok
14:26:23.910 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 视频文件[D:/001.MOV]转换成功
```

此时，接口返回信息如下：

```json
{
  "flag": "success",
  "message": "Convert Video to MP4 success."
}
```

查看输出文件夹“D:/cvtest/”，即可看到“001-online.mp4”已生成完毕。

## 输出：ftp方式

输入path方式，输出ftp方式，即从本地指定位置读取源视频文件，转换后将文件上传到FTP服务器指定的文件夹，且文件名按照传入的文件名命名。

此种方式需要业务系统启动一个FTP服务，将文件存储路径挂接到此FTP服务中，并且为“视频转换MP4服务”开放一个可访问此文件夹的账号。

传入的JSON示例如下：

```json
{
	"inputType": "path",
	"inputFile": "D:/001.MOV",
	"mp4FileName": "001-online",
	"writeBackType": "ftp",
	"writeBack": {
		"host": "127.0.0.1",
         "port": "21",
         "username": "zz",
         "password": "zz",
         "basepath": "/mp4/",
         "filepath": "/2021/10/"
	}
}
```

转换执行时，系统控制台输出如下内容：

```
14:23:59.902 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 直接转成mp4格式
ffmpeg …………
………………
14:25:34.916 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - ok
14:26:23.910 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 视频文件[D:/001.MOV]转换成功
```

此时，接口返回信息如下：

```json
{
  "flag": "success",
  "message": "Upload MP4 file to FTP success."
}
```

查看FTP服务器文件夹“/mp4/2021/10/”，即可看到“001-online.mp4”已上传完毕。

## 输出：url方式

输入path方式，输出url方式，即从本地指定位置读取源视频文件，转换后调用业务系统提供的“文件上传API”，将文件上传到业务系统中。

此种方式需要业务系统定制开发一个文件上传API接口，并且返回JSON结构的消息。返回信息示例如下：

```json
{
  "flag": "success",
  "message": "Upload MP4 file success."
}
```

传入的JSON示例如下：

```json
{
	"inputType": "path",
	"inputFile": "D:/001.MOV",
	"mp4FileName": "001-online",
	"writeBackType": "url",
	"writeBack": {
		"url": "http://127.0.0.1/upload.do"
	},
	"writeBackHeaders": {
		"Authorization": "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0"
	}
}
```

转换执行时，系统控制台输出如下内容：

```
14:23:59.902 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 直接转成mp4格式
ffmpeg …………
………………
14:25:34.916 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - ok
14:26:23.910 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 视频文件[D:/001.MOV]转换成功
```

此时，接口返回信息如下：

```json
{
  "flag": "success",
  "message": "Upload MP4 file success."
}
```



# 输入：url方式

## 输出：Path方式

输入url方式，输出Path方式，即从指定的URL地址下载视频文件，转换后将文件直接写入到指定的目标文件夹，且文件名按照传入的文件名命名。

如果业务系统的附件可以通过http方式直接访问（例如，采用对象存储、S3），则可以使用此种方式获取源视频文件。视频转换服务将文件下载到本地磁盘中后，将格式转换为MP4，再写入到指定的文件夹中。

传入的JSON示例如下：

```json
{
	"inputType": "url",
	"inputFile": "http://127.0.0.1/001.MOV",
	"mp4FileName": "001-online",
	"writeBackType": "path",
	"writeBack": {
		"path": "D:/cvtest/"
	}
}
```

转换执行时，系统控制台输出如下内容：

```
14:23:59.902 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 直接转成mp4格式
ffmpeg …………
……………………
14:25:34.916 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - ok
14:26:23.910 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 视频文件[http://127.0.0.1/001.MOV]转换成功
```

此时，接口返回信息如下：

```json
{
  "flag": "success",
  "message": "Convert Video to MP4 success."
}
```

查看输出文件夹“D:/cvtest/”，即可看到“001-online.mp4”已生成完毕。

## 输出：ftp方式

输入url方式，输出ftp方式，即从指定的URL地址下载视频文件，转换后将文件上传到FTP服务器指定的文件夹，且文件名按照传入的文件名命名。

如果业务系统的附件可以通过http方式直接访问（例如，采用对象存储、S3），则可以使用此种方式获取源视频文件。视频转换服务将文件下载到本地磁盘中后，将格式转换为MP4，再上传到FTP服务的指定文件夹中。

此种方式需要业务系统启动一个FTP服务，将文件存储路径挂接到此FTP服务中，并且为“视频转换MP4服务”开放一个可访问此文件夹的账号。

传入的JSON示例如下：

```json
{
	"inputType": "url",
	"inputFile": "http://127.0.0.1/001.MOV",
	"mp4FileName": "001-online",
	"writeBackType": "ftp",
	"writeBack": {
		"host": "127.0.0.1",
         "port": "21",
         "username": "zz",
         "password": "zz",
         "basepath": "/mp4/",
         "filepath": "/2021/10/"
	}
}
```

转换执行时，系统控制台输出如下内容：

```
14:23:59.902 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 直接转成mp4格式
ffmpeg …………
………………
14:25:34.916 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - ok
14:26:23.910 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 视频文件[http://127.0.0.1/001.MOV]转换成功
```

此时，接口返回信息如下：

```json
{
  "flag": "success",
  "message": "Upload MP4 file to FTP success."
}
```

查看FTP服务器文件夹“/mp4/2021/10/”，即可看到“001-online.mp4”已上传完毕。

## 输出：url方式

输入url方式，输出url方式，即从指定的URL地址下载视频文件，转换后调用业务系统提供的“文件上传API”，将文件上传到业务系统中。

如果业务系统的附件可以通过http方式直接访问（例如，采用对象存储、S3），则可以使用此种方式获取源视频文件。视频转换服务将文件下载到本地磁盘中后，将格式转换为MP4，再调用API接口，将文件上传到业务系统中。

此种方式需要业务系统定制开发一个文件上传API接口，并且返回JSON结构的消息。返回信息示例如下：

```json
{
  "flag": "success",
  "message": "Upload MP4 file success."
}
```

传入的JSON示例如下：

```json
{
	"inputType": "url",
	"inputFile": "http://127.0.0.1/001.MOV",
	"mp4FileName": "001-online",
	"writeBackType": "url",
	"writeBack": {
		"url": "http://127.0.0.1/upload.do"
	},
	"writeBackHeaders": {
		"Authorization": "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0"
	}
}
```

转换执行时，系统控制台输出如下内容：

```
14:23:59.902 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 直接转成mp4格式
ffmpeg …………
………………
14:25:34.916 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - ok
14:26:23.910 [http-nio-8080-exec-1] INFO  com.thinkdifferent.convertvideo.utils.ConvertVideoUtils - 视频文件[http://127.0.0.1/001.MOV]转换成功
```

此时，接口返回信息如下：

```json
{
  "flag": "success",
  "message": "Upload MP4 file success."
}
```

# 回调接口

业务系统可以提供一个GET方式的回调接口，在视频文件转换、回写完毕后，本服务可以调用此接口，传回处理的状态。例如：

```http
http://10.11.12.13/callback.do?file=001-online&flag=success
```

在传入JSON参数时，加入如下内容，即可由系统自动回调。

```json
	"callBackURL": "http://10.11.12.13/callback.do"
```

回调接口需要接收两个参数：

- file：处理后的文件名。本例为“001-online”。
- flag：处理后的状态，值为：success 或 error。