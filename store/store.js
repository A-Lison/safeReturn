import {
        observable,
        action
} from 'mobx-miniprogram'

export const store = observable({
        userDrive: {},
        numA: 1,
        numB: 2,
        // 计算属性
        get sum() {
                return this.numA + this.numB;
        },
        // action方法：更新store中的数据
        updateNumA: action(function (step) {
                this.numA += step;
        }),
        changeUserDrive: action(function (data) {
                this.userDrive = data;
        })
})