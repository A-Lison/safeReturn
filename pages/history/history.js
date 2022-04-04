// pages/history/history.js
Page({

        /**
         * 页面的初始数据
         */
        data: {
                // 警告数据
                record: [{
                                id: 0,
                                date: "06 - 21",
                                type: "喝酒",
                                times: 10,
                                imgUrl: "",
                                result: "酒后驾驶",
                                resultId: 0
                        },
                        {
                                id: 1,
                                date: "06 - 17",
                                type: "闭眼",
                                times: 10,
                                imgUrl: "",
                                result: "疲劳驾驶",
                                resultId: 1
                        },
                        {
                                id: 2,
                                date: "05 - 17",
                                type: "打哈欠",
                                times: 10,
                                imgUrl: "",
                                result: "疲劳驾驶",
                                resultId: 1
                        },
                        {
                                id: 0,
                                date: "02 - 21",
                                type: "抽烟",
                                times: 5,
                                imgUrl: "",
                                result: "疲劳驾驶",
                                resultId: 1
                        },
                ]
        },

        /**
         * 生命周期函数--监听页面加载
         */
        onLoad: function (options) {

        },

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