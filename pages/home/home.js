// pages/home/home.js
Page({
        data: {
                // 简介数据
                TabCur: 0,
                scrollLeft: 0,
                tabTitle: ["介绍", "酒驾监控", "状态监控", "关于"],
                elements: [{
                                title: '亲属绑定',
                                name: 'relative',
                                color: 'cyan',
                                icon: 'newsfill'
                        },
                        {
                                title: '酒精检测',
                                name: 'wine',
                                color: 'blue',
                                icon: 'colorlens'
                        },
                        {
                                title: '状态检测',
                                name: 'pose',
                                color: 'purple',
                                icon: 'font'
                        },
                        {
                                title: '及时报警 ',
                                name: 'call',
                                color: 'mauve',
                                icon: 'icon'
                        },

                ],
                // 轮播图数据
                cardCur: 0,
                swiperList: [{
                        id: 0,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big84000.jpg'
                }, {
                        id: 1,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big84001.jpg',
                }, {
                        id: 2,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big39000.jpg'
                }, {
                        id: 3,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10001.jpg'
                }, {
                        id: 4,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big25011.jpg'
                }, {
                        id: 5,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big21016.jpg'
                }, {
                        id: 6,
                        type: 'image',
                        url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big99008.jpg'
                }],
                // 状态监控数据
                iconList: [{
                        icon: 'cardboardfill',
                        color: 'red',
                        times: 120,
                        name: '闭眼'
                }, {
                        icon: 'recordfill',
                        color: 'orange',
                        times: 1,
                        name: '打哈欠'
                }, {
                        icon: 'picfill',
                        color: 'yellow',
                        times: 0,
                        name: '打电话'
                }, {
                        icon: 'noticefill',
                        color: 'olive',
                        times: 22,
                        name: '抽烟'
                }, {
                        icon: 'upstagefill',
                        color: 'cyan',
                        times: 0,
                        name: '注意力不集中'
                }, {
                        icon: 'clothesfill',
                        color: 'blue',
                        times: 0,
                        name: '东张西望'
                }],
                gridCol: 3,
                skin: false
        },
        //简介导航
        tabSelect(e) {
                this.setData({
                        TabCur: e.currentTarget.dataset.id,
                        scrollLeft: (e.currentTarget.dataset.id - 1) * 60
                })
                // console.log(this.data.TabCur);
        },
        /**
         * 生命周期函数--监听页面加载
         */
        onLoad: function (options) {
                this.towerSwiper('swiperList');

        },
        DotStyle(e) {
                this.setData({
                        DotStyle: e.detail.value
                })
        },
        // cardSwiper
        cardSwiper(e) {
                this.setData({
                        cardCur: e.detail.current
                })
        },
        // towerSwiper
        // 初始化towerSwiper
        towerSwiper(name) {
                let list = this.data[name];
                for (let i = 0; i < list.length; i++) {
                        list[i].zIndex = parseInt(list.length / 2) + 1 - Math.abs(i - parseInt(list.length / 2))
                        list[i].mLeft = i - parseInt(list.length / 2)
                }
                this.setData({
                        swiperList: list
                })
        },
        // towerSwiper触摸开始
        towerStart(e) {
                this.setData({
                        towerStart: e.touches[0].pageX
                })
        },
        // towerSwiper计算方向
        towerMove(e) {
                this.setData({
                        direction: e.touches[0].pageX - this.data.towerStart > 0 ? 'right' : 'left'
                })
        },
        // towerSwiper计算滚动
        towerEnd(e) {
                let direction = this.data.direction;
                let list = this.data.swiperList;
                if (direction == 'right') {
                        let mLeft = list[0].mLeft;
                        let zIndex = list[0].zIndex;
                        for (let i = 1; i < list.length; i++) {
                                list[i - 1].mLeft = list[i].mLeft
                                list[i - 1].zIndex = list[i].zIndex
                        }
                        list[list.length - 1].mLeft = mLeft;
                        list[list.length - 1].zIndex = zIndex;
                        this.setData({
                                swiperList: list
                        })
                } else {
                        let mLeft = list[list.length - 1].mLeft;
                        let zIndex = list[list.length - 1].zIndex;
                        for (let i = list.length - 1; i > 0; i--) {
                                list[i].mLeft = list[i - 1].mLeft
                                list[i].zIndex = list[i - 1].zIndex
                        }
                        list[0].mLeft = mLeft;
                        list[0].zIndex = zIndex;
                        this.setData({
                                swiperList: list
                        })
                }
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