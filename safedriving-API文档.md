#### 一些说明

API的notion在线文档：https://www.notion.so/fc9600-safedriving-API-e4842ad3e06f4fa7a2e24ff7a42f7c3c

API:http://119.91.89.21:8081

id：用户的唯一区分（计划用小程序可以直接获取的openid）

时间格式：“YYYY-MM-DD HH:MM:SS”             例如：“2017-03-02 15:22:22”

#### 用户信息

###### 获取用户信息

可以用于刷新信息（比如上拉刷新）

**请求url：**

- `/api/userInfo/search/{id}`

**请求方式：**

- get

**参数：**

~~~js
id:"<string>"
~~~

**返回值：**

请求成功会返回用户的信息

~~~js

{
	"data": {
    	//id号
    	"id":"<string>",
    	//名字
    	"name":"<string>",
    	//年龄
    	"age":<int>,
    	//性别
    	"sex":<int>,
    	//电话
    	"phone":"string"
	},
	"code": 0,
	"msg": "查询成功"
}
~~~

**请求失败：**

~~~js
{
	"data": null,
	"code": -1,
	"msg": "未查询到该用户"
}
~~~



###### 用户登录/注册

会先查找是否在数据库中，不在则注册为新的用户

**请求url：**

- `/api/userInfo/login/{id}/{num}`

**请求方式：**

- get

**参数：**

~~~js
//id号
id:"<string>"
//电话
num:"<string>"
~~~

**返回值：**

已存在的

~~~js
{
	"data": {
    	//id号
    	"id":"<string>",
   	 	//名字
    	"name":"<string>",
    	//年龄
    	"age":<int>,
    	//性别
    	"sex":<int>,
    	//电话
    	"phone":"string"
	},
	"code": 0,
	"msg": "登陆成功"
}
~~~

新创建的

~~~js
{
	"data": {
    	//id号
    	"id":"<string>",
   	 	//名字
    	"name":"<string>",
    	//年龄
    	"age":<int>,
    	//性别
    	"sex":<int>,
    	//电话
    	"phone":"string"
	},
	"code": 1,
	"msg": "用户创建成功"
}
~~~



###### 用户更新信息

**请求url：**

- `/api/userInfo/update`

**请求方式：**

- post

**参数：**

~~~js
{
    //id号
    "id":"<string>",
    //名字
    "name":"<string>",
    //年龄
    "age":<int>,
    //性别
    "sex":<int>,
    //电话
    "phone":"string"
}
~~~

**返回值：**

更新成功

~~~js
true
~~~

#### **亲属信息**

###### **添加亲属**

**请求url**

- `api/relation/add`

**请求方式：**

post

**参数**

~~~js
{	
    "id":"<string>",
    //亲属名字
    "relation":"<string>",
    //亲属电话
    "num":"<string>"
}
~~~

**返回值**

~~~js
true//添加成功
~~~

~~~js
false//添加失败,重名
~~~



###### 删除亲属

**请求url：**

- `relation/delete/{relation}/{id}`

**请求方式：**

get

**参数：**

~~~js
//亲属名
"relation":"<string>"
//id号
"id":"<string>"
~~~

**返回值：**

~~~js
true
~~~



###### **查询亲属**

**请求url：**

- `api/relation/list/{id}`

**请求方式：**

get

**参数：**

~~~js
//id号
"id":"<string>"
~~~

**返回值：**

如果已绑定亲属

~~~js
{
	"data": [
		{
			"relation": "<string>",
			"phone": "<string>"
		},
        ...,
		{
			"relation": "<strng>",
			"phone": "<string>"
		}
	],
	"code": 0,
	"msg": "查询亲属关系成功"
}
~~~

未绑定亲属关系

~~~
{
	"data: [],
	"code": -1,
	"msg": "还没有绑定亲属"
}
~~~



###### **修改亲属**

**请求url：**

- `api/relation/change`

**请求方式：**

post

**参数：**

~~~js
{
    //id号
	"id":"<string>",
	//新亲属名
	"newRe":"<string>",
	//新电话
	"phone":"<string>",
	//旧亲属名
	"oldRe":"<string>"
}
~~~

**返回值：**

~~~js
true
~~~



#### 健康数据

###### 增加记录

数据传输使用分段的方式进行，详细格式见参数的模板，本地可以先存储一段时间的数据，然后定时调用api发送数据即可，因为是分段传输数据，所以会有（sign）这个标志标明这是否为最后一段数据（用户的驾驶是否结束，后端会给数据库中相应的数据进行标记表明这段记录是这次行车时记录的）

**请求url：**

- `api/health/healthAdd`

**请求方式：**

post

**参数**

~~~js

{
	"sign":int,		// 1：这不是最后一段数据 0：这是最后一段数据	
	"id":"<string>",
	"data":[{
        //心跳
		"heart":<int>,
		//血压
        "press":<double>,
		//酒精
        "alcohol":<double>,
		//体温
        "heat":<double>,
        //时间戳
		"time":"<string>"
        //经度
        "lnogitude":<double>,
        //纬度
        "latitude":<double>
		},
        ...
		{
		"heart":<int>,
		"press":<double>,
		"alcohol":<double>,
		"heat":<double>,
		"time":"<string>",
        "lnogitude":<double>,
        "latitude":<double>
	}]
}
~~~

**返回值**

结束

~~~js
{
	"data": null,
	"code": 0,
	"msg": "数据传输结束"
}
~~~

未结束

~~~js
{
	"data": null,
	"code": 1,
	"msg": "运行中"
}
~~~



###### 检查传输是否完成

用于在小程序中途退出（比如手机息屏清后台，不小心划掉了）再次打开的情况，后端确认上次传输未完全实现的情况下返回相关参数，小程序可根据参数标识（code）再将本地的缓存的部分继续上传，可以在每次小程序打开的时候调用一次

**请求url：**

- `api/health/check/{id}`

**请求方式：**

get

**参数**

~~~js
id:"<string>"
~~~

**返回值**

数据传输未发生异常

~~~js
{
	"data": null,
	"code": 0,
	"msg": "数据传输成功"
}
~~~

数据传输发生异常

~~~js
{
	"data": null,
	"code": -1,
	"msg": "上次传输未正常结束"
}
~~~



###### 查询所有批次的健康数据

**请求url:**

- `api/health/searchAll/{id}`

**请求方式**

get

**参数**

~~~js
id:"<string>"
~~~

**返回值**

~~~js
{
	"data": [
        //num的格式例如"2015-03-02 18:22:22"，默认以最后一次记录标明每一组数据
		{
			"num": "<string>"
		},
		{
			"num": "<string>"
		}
	],
	"code": <int>,//批次的总数
	"msg": "所有的记录"
}
~~~



###### 查询近n天所有批次的健康数据

如果需要查询近一个月或者进一个星期等时间段的批次，设置n的值即可

**请求url:**

- `api/health/searchN/{id}/{n}`

**请求方式**

get

**参数**

~~~js
id:"<string>"
n:<int>	//时间段包括的天数
~~~

**返回值**

~~~js
{
	"data": [
        //num的格式例如"2015-03-02 18:22:22"，默认以最后一次记录标明每一组数据
		{
			"num": "<string>"
		},
		{
			"num": "<string>"
		}
	],
	"code": <int>,//批次的总数
	"msg": "近n天的记录"
}
~~~



###### 按照年月日查找相关批次的健康数据

得到要搜索的年月日的值，可以为0（为零表示在这个参数上不做约束）

**请求url:**

- `api/health/search`

**请求方式**

post

**参数**

~~~js
{
	id:"<string>",
    //年
	year:<int>,
	//月
    month:<int>,
	//日
    day:<int>
}
~~~

**返回值**

~~~js
{
	"data": [
        //num的格式例如"2015-03-02 18:22:22"，默认以最后一次记录标明每一组数据
		{
			"num": "<string>"
		},
		{
			"num": "<string>"
		}
	],
	"code": <int>,//批次的总数
	"msg": "搜索到的记录"
}
~~~



###### 查询具体某一批次的所有健康数据

上文提到的查询api将相关的批次列出来后，如果用户点击相应的批次，在调用这个api可以将具体的数据返回小程序

**请求url：**

- `api/health/list/{id}/{num}`

**请求方式**

get

**参数**

~~~js
id:"<string>"
num:"<string>"//num为用户点击的相应批次的num
~~~

**返回值**

~~~js
{
	"data": [
		{
			"num": "<string>",	//批次
			"time": "<string>",	//时间
			"heart": <int>,	//心跳
			"press": <double>,	//血压
			"alcohol": <double>,	//酒精
			"heat": <double>	//体温
            "lnogitude":<double>,	//经度
        	"latitude":<double>	//纬度
		},
		...,
		{
			"num": "<string>",	//批次
			"time": "<string>",	//时间
			"heart": <int>,	//心跳
			"press": <double>,	//血压
			"alcohol": <double>,	//酒精
			"heat": <double>	//体温
        	"lnogitude":<double>,	//经度
        	"latitude":<double>	//纬度
		}
	],
	"code": <int>,	//数据总数
	"msg": "num的健康数据"	//如：2015-03-02 18:22:22的健康数据
}
~~~



#### 危险行为

将文件上传到服务器，并将可以直接通过url访问的地址存入数据库，相关的请求图片的api会将对应的url返回，可以直接使用

###### 上传图片

**请求url:**

- `api/pic/upload`

**请求方式**

post

**参数**

~~~js
{
    //文件（这里是图片）
    "file":"<MultipartFile>"，
    //id号
    "id":"<string>",
    //违规类型	 1：抽烟 2：喝东西 3：打电话 4.疲劳驾驶
    "type":<int>,
    //时间戳		
    "time":"<string>"
}
~~~

**返回值**

~~~js
true
~~~



###### 查询所有批次的危险记录

**请求url:**

- `api/pic/searchAll/{id}`

**请求方式**

get

**参数**

~~~js
id:"<string>"
~~~

**返回值**

~~~js
{
	"data": [
        //num的格式例如"2015-03-02 18:22:22"，默认以最后一次记录标明每一组数据
		{
			"num": "<string>"
		},
		{
			"num": "<string>"
		}
	],
	"code": <int>,//批次的总数
	"msg": "所有的危险记录"
}
~~~



###### 查询近n天所有批次的危险记录

如果需要查询近一个月或者进一个星期等时间段的批次，设置n的值即可

**请求url:**

- `api/pic/searchN/{id}/{n}`

**请求方式**

get

**参数**

~~~js
id:"<string>"
n:<int>	//时间段包括的天数
~~~

**返回值**

~~~js
{
	"data": [
        //num的格式例如"2015-03-02 18:22:22"，默认以最后一次记录标明每一组数据
		{
			"num": "<string>"
		},
		{
			"num": "<string>"
		}
	],
	"code": <int>,//批次的总数
	"msg": "近n天的危险记录"
}
~~~



###### 按照年月日查找相关批次的健康数据

得到要搜索的年月日的值，可以为0（为零表示在这个参数上不做约束）

**请求url:**

- `api/pic/search`

**请求方式**

post

**参数**

~~~js
{
	id:"<string>",
    //年
	year:<int>,
	//月
    month:<int>,
	//日
    day:<int>
}
~~~

**返回值**

~~~js
{
	"data": [
        //num的格式例如"2015-03-02 18:22:22"，默认以最后一次记录标明每一组数据
		{
			"num": "<string>"
		},
		{
			"num": "<string>"
		}
	],
	"code": <int>,//批次的总数
	"msg": "搜索到的危险记录"
}
~~~



###### 查询具体某一批次的所有健康数据

上文提到的查询api将相关的批次列出来后，如果用户点击相应的批次，在调用这个api可以将具体的数据返回小程序

**请求url：**

- `api/pic/list/{id}/{num}`

**请求方式**

get

**参数**

~~~js
id:"<string>"
num:"<string>"//num为用户点击的相应批次的num
~~~

**返回值**

~~~json
{
	"data": [
		{
            //批次
			"num": "<string>",
            //时间
			"time": "<string>",
			//危险类型
            "type": <int>,
			//图片的url
            "img": "<string>"
            //经度
           	"lnogitude":<double>,	
        	//纬度	
        	"latitude":<double>	
		},
		...,
		{
			//批次
			"num": "<string>",
            //时间
			"time": "<string>",
			//危险类型
            "type": <int>,
			//图片的url
            "img": "<string>"
        	//经度
        	"lnogitude":<double>,	
        	//纬度	
        	"latitude":<double>	
		}
	],
	"code": <int>,	//数据总数
	"msg": "num的危险记录"	//如：2015-03-02 18:22:22的健康数据
}
~~~

#### 生成健康报表

###### excel文件下载链接

将时间在num1和num2之间的车次导出为一张excel表格，表格下每一张表的表示一次车次的记录

**请求url：**

- `api/excel/export/{id}/{num1}/{num2}`

**请求方式**

get

**参数**

~~~json
"id":"<string>"
"num1":"<string>"	//时间，起始时间
"num2":"<string>"	//时间，截止时间
~~~

**返回值**

~~~json
//游览器打开链接直接下载文件
~~~

#### 一些连接相关

###### 算法方面获取用户端的相应id

**请求url:**

- `api/userInfo/getId`

**请求方式**

get

**参数**

~~~json
//暂时无
~~~

**返回值**

~~~json
{
	"data":"<string>"//当前使用的小程序的id
	"code": 0,
	"msg": "登陆成功"
}
~~~

###### 酒精参数中转——暂存

**请求url：**

- `api/health/sendAlco/{mes}`

**请求方式**

get

**参数**

~~~json
"mes":"<string>" //需要传的信息
~~~

**返回值**

~~~json
true
~~~

###### 酒精参数中转——取出

**请求url：**

- `api/health/getAlco`

**请求方式**

get

**参数**

~~~json
//暂时无
~~~

**返回值**

~~~json
"<string>"	//暂存的信息
~~~

