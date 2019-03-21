<template>
<div>
    <el-dialog
      title="创建群组"
      :visible.sync="showGroupBox"
      @close='closeDialog'
      width="50%"
      center
      :modal-append-to-body='false'
      :show-close='true'>
      <div class="box-wrapper login100-form validate-form">
        <div class="wrap-input100 validate-input" ref="group-dom" data-validate = "群组账号至少为4位且为数字或字母">
            <input v-model="groupnameText" class="input100" @focus="focusHandle('group', $event)" @blur="blurHandle('group', $event)" type="text" name="email">
            <span class="focus-input100" data-placeholder="群组账号"></span>
        </div>

        <div class="wrap-input100 validate-input" ref="nick-dom" data-validate="群组昵称至少为4位">
            <input v-model='nicknameText' class="input100" @focus="focusHandle('nick', $event)" @blur="blurHandle('nick', $event)" name="pass" maxlength="10">
            <span class="focus-input100" data-placeholder="群组昵称"></span>
        </div>
        <div class="wrap-input100 validate-input" ref="desc-dom">
            <input v-model="descText" class="input100"  @blur="blurHandle('desc', $event)" type="text" name="email" maxlength="20">
            <span class="focus-input100" data-placeholder="群简介"></span>
        </div>
        <div class="container-login100-form-btn"  @click="createGroup">
            <div class="wrap-login100-form-btn">
                <div class="login100-form-bgbtn"></div>
                <button class="login100-form-btn">
                    创建群组
                </button>
            </div>
        </div>
      </div>

    </el-dialog>
    <div class="add-group el el-icon-add" title="创建群组" @click="showBox"></div>
</div>
</template>
<script type="text/javascript">
    import $axios from 'axios';
    import { mapActions } from 'vuex';
    import Common from "../assets/scripts/common.js";

    export default {
        name: 'createGroup',
        data() {
            return {
                showGroupBox: false,
                groupname: '',
                nickname: '',
                desc: '',
                groupnameText: '',
                nicknameText: '',
                descText: ''
            }
        },
        methods: {
            ...mapActions([
            'updateGroupList',
            'updateGroupFriendList'
          ]),
            closeDialog: function () {

            },
            showBox: function () {
                this.showGroupBox = true;
            },
            blurHandle: function (dom, event) {
                var $target = $(event.target);
                var regUser = /^[a-zA-Z0-9_-]{4,16}$/;
                var regPwd = /^[a-zA-Z0-9_-]{6,20}$/;
                var regNick = /^[a-zA-Z\d\u4e00-\u9fa5]$/;
                var val = $target.val();
                var $dom = $(this.$refs[dom + '-dom']);
                var that = this;
                if (val != '') {
                    $target.addClass('has-val');
                } else {
                    $target.removeClass('has-val');
                }
                if (dom == 'group' && !regUser.test(val)) {
                    $dom.addClass('alert-validate');
                } else if (dom == 'group' && regUser.test(val)) {
                    that.groupname = val;
                    $dom.removeClass('alert-validate');
                }
                if (dom == 'nick' && regNick.test(val)) {
                    $dom.addClass('alert-validate');
                } else if (dom == 'nick' && !regNick.test(val)) {
                    that.nickname = val;
                    $dom.removeClass('alert-validate');
                }
                if (dom == 'pwd' && !regPwd.test(val)) {
                    $dom.addClass('alert-validate');
                } else if (dom == 'pwd' && regPwd.test(val)) {
                    that.pwd = val;
                    $dom.removeClass('alert-validate');
                }
                if (dom == 'desc') {
                    that.desc = val;
                    $dom.removeClass('alert-validate');
                }
            },
            focusHandle: function (dom, event) {
                var $target = $(event.target);
                var $dom = $(this.$refs[dom + '-dom']);
                $dom.removeClass('alert-validate');
            },
            createGroup: function () {
                let that = this;
                if (that.groupname != '' && that.nickname != '') {
                    $axios.post('/api/group/create', {
                        "groupName": (that.groupname).replace(/\s/g, ''),
                        "groupNickname": (that.nickname).replace(/\s/g, ''),
                        "introduction": that.desc
                    }).then((res) => {
                        if (res && res.data && !res.data.code) {
                            that.$notify({
                                title: '提示',
                                message: that.groupname + "创建成功~",
                                type: 'success'
                            });
                            that.showGroupBox = false;
                            that.groupname = '';
                            that.nickname = '';
                            that.desc = '';
                            that.groupnameText = "";
                            that.nicknameText = "";
                            that.descText = "";
                            $(that.$refs['group-dom']).find('input').removeClass('has-val');
                            $(that.$refs['nick-dom']).find('input').removeClass('has-val');
                            $(that.$refs['desc-dom']).find('input').removeClass('has-val');
                            // that.$store.commit('UPDATE_GROUPSESSIONS_LIST', res.data.data);

                            let result = res.data.data || {};
                            let tempList = [];
                            let tempItem = {
                                "groupId": result.groupId || result._id,
                                "groupHeadImg": result.headImg || result.groupHeadImg,
                                "groupName": result.groupName,           // * 群组帐号
                                "groupNickname": result.groupNickname || '' ,        // * 群组名称
                                "introduction": result.introduction || '',
                                "ownerUserId": result.ownerUserId,
                                "isRead": true
                            };
                            tempList.push(tempItem)
                            that.updateGroupList({
                                list: tempList,
                                type: 0
                            });

                            // 更新群组成员
                            that.getGroupUserList(result.groupId);

                            // that.$store.commit('UPDATE_GROUP_FRIENDLIST', friendList);
                        } else {
                            that.$message({
                                message: res.data.message,
                                type:'warning'
                            });
                            return false;
                        }
                    }).catch((res) => {
                      that.$notify.error({
                        title: '提示',
                        message: res.message,
                        duration: 4000
                      });
                    });
                }
            },

            // 更新群成员列表
            getGroupUserList: function (groupId) {
                var that  = this;
                Common.axios({
                    url: 'getGroupFriendList',
                    data: {
                        groupId: groupId
                    }
                }).then((res) => {
                    if (res.data) {
                        that.updateGroupFriendList(res.data[groupId]);
                    }
                }, (error) => {

                });
            },
        }

    }
</script>
<style type="text/css" lang="scss" scoped>
    .box-wrapper {
        margin: 0.1rem auto;
    }
    .add-group {
        padding-left: 0.1rem;
        margin-top: 0.03rem;
        line-height: 0.3rem;
        font-size: 0.12rem;
        cursor:pointer;
    }
        /*------------------------------------------------------------------
    [ Form ]*/
    .wrap-login100-form-btn {
        width: 70%;
        display: block;
        position: relative;
        z-index: 1;
        border-radius: 25px;
        overflow: hidden;
        margin: 0 auto;
    }
    .login100-form {
      width: 70%;
    }

    .login100-form-title {
      display: block;
      font-family: Poppins-Bold;
      font-size: 30px;
      color: #333333;
      line-height: 1.2;
      text-align: center;
    }
    .login100-form-title i {
      font-size: 60px;
    }

    /*------------------------------------------------------------------
    [ Input ]*/

    .wrap-input100 {
      width: 100%;
      position: relative;
      border-bottom: 2px solid #adadad;
      margin-bottom: 37px;
    }

    .input100 {
        outline: none;
        border: none;
        font-size: 16px;
        color: #555555;
        line-height: 1.2;
        display: block;
        width: 100%;
        height: 0.5rem;
        background: transparent;
        padding: 0 0.05rem;
        text-align: center;
    }

    /*---------------------------------------------*/
    .focus-input100 {
      text-align: center;
      position: absolute;
      display: block;
      width: 100%;
      height: 100%;
      top: 0;
      left: 0;
      pointer-events: none;
    }

    .focus-input100::before {
      content: "";
      display: block;
      position: absolute;
      bottom: -2px;
      left: 0;
      width: 0;
      height: 2px;

      -webkit-transition: all 0.4s;
      -o-transition: all 0.4s;
      -moz-transition: all 0.4s;
      transition: all 0.4s;

      background: #6a7dfe;
      background: -webkit-linear-gradient(left, #21d4fd, #b721ff);
      background: -o-linear-gradient(left, #21d4fd, #b721ff);
      background: -moz-linear-gradient(left, #21d4fd, #b721ff);
      background: linear-gradient(left, #21d4fd, #b721ff);
    }

    .focus-input100::after {
      font-family: Poppins-Regular;
      font-size: 15px;
      color: #999999;
      line-height: 1.2;

      content: attr(data-placeholder);
      display: block;
      width: 100%;
      position: absolute;
      top: 16px;
      left: 0px;
      padding-left: 5px;

      -webkit-transition: all 0.4s;
      -o-transition: all 0.4s;
      -moz-transition: all 0.4s;
      transition: all 0.4s;
    }

    .input100:focus + .focus-input100::after {
      top: -15px;
    }

    .input100:focus + .focus-input100::before {
      width: 100%;
    }

    .has-val.input100 + .focus-input100::after {
      top: -15px;
    }

    .has-val.input100 + .focus-input100::before {
      width: 100%;
    }

    /*[ Button ]*/
    .container-login100-form-btn {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -ms-flexbox;
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      padding-top: 13px;
    }

    .wrap-login100-form-btn {
        width: 100%;
        display: block;
        position: relative;
        z-index: 1;
        border-radius: 0.25rem;
        overflow: hidden;
        margin: 0 auto;
    }

    .login100-form-bgbtn {
        position: absolute;
        z-index: -1;
        width: 300%;
        height: 100%;
        background: linear-gradient(to right, #21d4fd, #b721ff, #21d4fd, #b721ff);
        top: 0;
        left: -100%;
        transition: all 0.4s;
    }

    .login100-form-btn {
        font-size: 16px;
        color: #fff;
        line-height: 1.2;
        text-transform: uppercase;

        display: flex;
        justify-content: center;
        align-items: center;
        padding: 0 0.2rem;
        width: 100%;
        height: 0.5rem;
        outline: none !important;
        border: none;
        background: transparent;
    }

    .wrap-login100-form-btn:hover .login100-form-bgbtn {
      left: 0;
    }


    /*------------------------------------------------------------------
    [ Alert validate ]*/

    .validate-input {
      position: relative;
    }

    .alert-validate::before {
      content: attr(data-validate);
      position: absolute;
      max-width: 70%;
      background-color: #fff;
      border: 1px solid #c80000;
      border-radius: 2px;
      padding: 0.05rem 0.25rem 0.05rem 0.1rem;
      top: 50%;
      transform: translateY(-50%);
      right: 0px;
      pointer-events: none;
      color: #c80000;
      font-size: 12px;
      line-height: 1.4;
      text-align: left;
      visibility: hidden;
      opacity: 0;
      transition: opacity 0.4s;
      visibility: visible;
      opacity: 1;
    }

    .alert-validate::after {
      content: "\e836";
      font-family: el;
      font-size: 18px;
      color: #c80000;

      display: block;
      position: absolute;
      background-color: #fff;
      top: 52%;
      transform: translateY(-50%);
      right: 0.05rem;
      visibility: visible;
      opacity: 1;
    }

    .alert-validate:hover:before {
      visibility: visible;
      opacity: 1;
    }

    .text-center {
        text-align: center;
        margin-top: 0.3rem;
    }
</style>