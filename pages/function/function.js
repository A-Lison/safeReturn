// pages/function/function.js
import {
        createStoreBindings
} from 'mobx-miniprogram-bindings';
import {
        store
} from '../../store/store';
Page({

        /**
         * 页面的初始数据
         */
        data: {
                isDrive: false, //是否开始驾驶
                loading: false,
                physicalData: {
                        id: 0,
                        temperature: 37,
                        heart: 65,
                        bloodPressure: 70,
                        bloodFat: 53
                },
                isWine: false,
                alcohol: 53,
                poseData: {
                        id: 0,
                        biYan: {
                                times: 0,
                                img: []
                        },
                        daHaQian: {
                                times: 10,
                                img: []
                        },
                        playPhone: {
                                times: 20,
                                img: []
                        },
                        chouYan: {
                                times: 30,
                                img: []
                        },
                        call: {
                                times: 40,
                                img: []
                        },
                        zhuYiLi: {
                                times: 50,
                                img: []
                        },
                        isTired: false
                }
        },
        startDrive(e) {
                if (JSON.stringify(this.data.userDrive) == "{}") {
                        wx.showToast({
                                title: '无驾驶员信息',
                                icon: "error",
                                success: () => {
                                        setTimeout(() => {
                                                wx.navigateTo({
                                                        url: '/pages/profile/profile',
                                                })
                                        }, 1500);
                                }
                        })
                } else {
                        this.setData({
                                loading: true
                        })
                        setTimeout(() => {
                                this.setData({
                                        isDrive: true,
                                        loading: false
                                })
                        }, 500);
                }


        },
        endDrive() {
                this.setData({
                        loading: true
                })
                setTimeout(() => {
                        this.setData({
                                isDrive: false,
                                loading: false
                        })
                }, 1000);
        },
        /**
         * 生命周期函数--监听页面加载
         */
        onLoad: function (options) {
                this.storeBindings = createStoreBindings(this, {
                        store,
                        fields: ['userDrive'],
                        actions: ['changeUserDrive']
                })
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