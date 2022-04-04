const chooseLocation = requirePlugin('chooseLocation');
const plugin = requirePlugin('routePlan');
// 引入SDK核心类
var QQMapWX = require("../../qqmap-wx-jssdk1.2/qqmap-wx-jssdk.min");
var qqmapsdk;
//使用在腾讯位置服务申请的key（必填）
const key = "ERKBZ-PM76F-JZYJE-NYNKL-QKKY6-KSF7V";
//调用插件的app的名称（必填）
const referer = "平安归来";
let endPoint = JSON.stringify({ //终点
        'name': '吉野家(北京西站北口店)',
        'latitude': 39.89631551,
        'longitude': 116.323459711
});
Page({
        data: {
                address: "",
                locationName: "",
                // map组件
                latitude: 23.099994,
                longitude: 113.324520,
                markers: [{
                        id: 1,
                        latitude: 23.099994,
                        longitude: 113.324520,
                        name: 'T.I.T 创意园'
                }],
                covers: [{
                        latitude: 23.099994,
                        longitude: 113.344520,
                        iconPath: '/image/location.png'
                }, {
                        latitude: 23.099994,
                        longitude: 113.304520,
                        iconPath: '/image/location.png'
                }]
        },
        onLoad: function () {
                // 实例化API核心类
                qqmapsdk = new QQMapWX({
                        key: 'ERKBZ-PM76F-JZYJE-NYNKL-QKKY6-KSF7V'
                });
        },
        onReady: function (e) {
                this.mapCtx = wx.createMapContext('myMap');
                this.mapCtx.moveToLocation(); //移动到当前位置
                this.getAddress();
        },
        getAddress() {
                this.getJingWeiDu();
        },
        /**
         * 获取经纬度
         */
        getJingWeiDu() {
                let that = this;
                wx.getLocation({
                        success(res) {

                                that.setData({
                                        longitude: res.longitude,
                                        latitude: res.latitude
                                }, () => {
                                        that.jingWeiduToDiZhi();
                                });

                        }
                })
        },
        /**
         * 经纬度转换成地址
         */
        jingWeiduToDiZhi() {
                let that = this;
                // 调用接口
                qqmapsdk.reverseGeocoder({
                        location: {
                                latitude: that.data.latitude,
                                longitude: that.data.longitude
                        },
                        success: function (res) {
                                that.setData({
                                        address: res.result.address,
                                        locationName: res.result.formatted_addresses.recommend,
                                })

                        },
                        fail: function (error) {
                                // console.log("err")
                                console.error(error);
                        },
                        complete: function (res) {
                                console.log(res);
                        }
                })
        },
        onShow: function () {
                // 从地图选点插件返回后，在页面的onShow生命周期函数中能够调用插件接口，取得选点结果对象
                // 如果点击确认选点按钮，则返回选点结果对象，否则返回null
                const location = chooseLocation.getLocation();
                if (location) {
                        this.setData({
                                address: location.address ? location.address : "",
                                locationName: location.name ? location.name : ""
                        });
                }
        },
        //显示地图
        showCurrentMap() {
                wx.navigateTo({
                        url: 'plugin://chooseLocation/index?key=' + key + '&referer=' + referer
                });
        },
        // 导航
        routePlan() {
                wx.navigateTo({
                        url: 'plugin://routePlan/index?key=' + key + '&referer=' + referer + '&endPoint=' + endPoint
                });
        }
});