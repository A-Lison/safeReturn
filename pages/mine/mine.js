const app = getApp()
Page({
        /**
         * 页面的初始数据
         */
        data: {
                StatusBar: app.globalData.StatusBar,
                CustomBar: app.globalData.CustomBar,
                isLOad: false,
                defultImg: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg',
                userInfo: {},
                hasUserInfo: false,
                // canIUse: wx.canIUse('button.open-type.getUserInfo'),
                canIUse: false,
        },
        // 跳到亲属绑定
        toRelative() {
                wx.navigateTo({
                        url: '/pages/relative/relative',
                })
        },
        // 跳到历史记录
        toHistory() {
                wx.navigateTo({
                        url: '/pages/history/history',
                })
        },
        // 跳到个人中心
        editUserMsg() {
                wx.navigateTo({
                        url: '/pages/profile/profile',
                })
        },
        //事件处理函数
        bindViewTap: function () {
                wx.navigateTo({
                        url: '../logs/logs'
                })
        },
        onLoad() {
                if (wx.getUserProfile) {
                        this.setData({
                                canIUse: true
                        })
                }
        },
        getUserProfile(e) {
                wx.getUserProfile({
                        desc: '用于获得用户信息',
                        success: (res) => {
                                this.setData({
                                        userInfo: res.userInfo,
                                        hasUserInfo: true
                                })
                        }
                })
        },
        // getUserInfo: function (e) {
        //         console.log(e)
        //         app.globalData.userInfo = e.detail.userInfo
        //         this.setData({
        //                 userInfo: e.detail.userInfo,
        //                 hasUserInfo: true,
        //         })
        // },
        // onLoad: function (options) {
        //         if (app.globalData.userInfo) {
        //                 console.log(111);
        //                 this.setData({
        //                         userInfo: app.globalData.userInfo,
        //                         hasUserInfo: true
        //                 })
        //         } else if (this.data.canIUse) {
        //                 console.log(222);
        //                 // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
        //                 // 所以此处加入 callback 以防止这种情况
        //                 app.userInfoReadyCallback = res => {
        //                         this.setData({
        //                                 userInfo: res.userInfo,
        //                                 hasUserInfo: true
        //                         })
        //                 }
        //         } else {
        //                 console.log(333);
        //                 // 在没有 open-type=getUserInfo 版本的兼容处理
        //                 wx.getUserInfo({
        //                         success: res => {
        //                                 app.globalData.userInfo = res.userInfo
        //                                 this.setData({
        //                                         userInfo: res.userInfo,
        //                                         hasUserInfo: true
        //                                 })
        //                         }
        //                 })
        //         }
        //         console.log(789);

        // },

        /**
         * 生命周期函数--监听页面初次渲染完成
         */
        onReady: function () {

        },

        /**
         * 生命周期函数--监听页面显示
         */
        onShow: function () {

        },

        /**
         * 生命周期函数--监听页面隐藏
         */
        onHide: function () {

        },

        /**
         * 生命周期函数--监听页面卸载
         */
        onUnload: function () {

        },

        /**
         * 页面相关事件处理函数--监听用户下拉动作
         */
        onPullDownRefresh: function () {

        },

        /**
         * 页面上拉触底事件的处理函数
         */
        onReachBottom: function () {

        },

        /**
         * 用户点击右上角分享
         */
        onShareAppMessage: function () {

        }
})