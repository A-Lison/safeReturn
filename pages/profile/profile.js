// pages/profile/profile.js
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
                date: '2018-12-25',
                user: {
                        name: "",
                        sex: "",
                        birthday: '1990-10-01',
                        age: "",
                        phone: 0,
                        sosPhone: 0
                }
        },
        /**
         * 生命周期函数--监听页面加载
         */
        onLoad: function (options) {
                this.storeBindings = createStoreBindings(this, {
                        store,
                        fields: ['userDrive', 'age'],
                        actions: ['changeUserDrive']
                })
        },
        getNameInput: function (e) {
                this.setData({
                        "user.name": e.detail.value
                })
                // this.changeUserDrive("name", e.detail.value)
        },
        getAgeInput(e) {
                this.setData({
                        "user.birthday": e.detail.value,
                })


        },
        getSexInput(e) {
                this.setData({
                        "user.sex": e.detail.value
                })
        },
        getPhoneInput(e) {
                this.setData({
                        "user.phone": e.detail.value
                })
        },
        getSosPhoneInput(e) {
                this.setData({
                        "user.sosPhone": e.detail.value
                })
        },
        postUserMsg() {
                this.changeUserDrive(this.data.user);
                wx.navigateBack();

        },

        /**
         * 生命周期函数--监听页面初次渲染完成
         */
        onReady: function () {
                wx.hideLoading();
        },

        /**
         * 生命周期函数--监听页面显示
         */
        onShow: function () {
                wx.showLoading();
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
                this.storeBindings.destroyStoreBindings();
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