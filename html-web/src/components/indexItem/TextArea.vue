<template>
    <div class="text-textarea">
        <textarea placeholder="按 Enter 发送" v-model="content" @keyup.enter="onKeyup"></textarea>
    </div>
</template>
<script type="text/javascript">
import {send2Friend, send2Group} from '../../assets/scripts/ws/msgSender.js';
export default {
    name: 'TextArea',
    data () {
        return {
            content: ''
        };
    },
    methods: {
        onKeyup (e) {
            let that = this;
            let tabIndex = this.$store.state.tabType;
            if (e.keyCode === 13 && that.content.length) {
                // this.$store.commit('SEND_MESSAGE', this.content);
                if (tabIndex == 1) {
                    let groupId = that.$store.state.currentGroupId;
                    if (!groupId) {
                        return false;
                    }
                    // 回复群组消息
                    send2Group(groupId, that.content, function (data) {
                      // alert(JSON.stringify(data));
                        let sendData = {};
                        sendData.content = that.content;
                        sendData.code = data.code;
                        that.$store.commit('SEND_GROUP_MESSAGE', sendData);
                        that.content = '';
                    });

                } else {
                    let toUerId = that.$store.state.currentSessionId;
                    if (!toUerId) {
                        return false;
                    }
                    // 回复好友消息
                    send2Friend(toUerId, that.content, function (data) {
                      // alert(JSON.stringify(data));
                        let sendData = {};
                        sendData.content = that.content;
                        sendData.code = data.code;
                        that.$store.commit('SEND_MESSAGE', sendData);
                        that.content = '';
                    });
                }
            }
        }
    }
}
</script>
<style type="text/css" lang="scss" scoped>
.text-textarea {
    height: 1.5rem;
    border-top: 1px solid #ddd;

    textarea {
        padding: 0.1rem;
        height: 80%;
        width: 100%;
        border: none;
        outline: none;
        font-family: "Micrsofot Yahei";
        resize: none;
        overflow-y: scroll;
    }
}
</style>