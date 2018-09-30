<template>
    <div class="container-login100">
        <div class="wrap-login100">
            <div class="login100-form validate-form">
                <div class="wrap-input100 validate-input" ref="user-dom" data-validate = "账号至少为4位且为数字或字母">
                    <input class="input100" @focus="focusHandle('user', $event)" @blur="blurHandle('user', $event)" type="text" name="email">
                    <span class="focus-input100" data-placeholder="账号"></span>
                </div>

                <div class="wrap-input100 validate-input" ref="pwd-dom" data-validate="密码至少为6位">
                    <input class="input100" @focus="focusHandle('pwd', $event)" @blur="blurHandle('pwd', $event)" type="password" name="pass">
                    <span class="focus-input100" data-placeholder="密码"></span>
                </div>

                <div class="container-login100-form-btn">
                    <div class="wrap-login100-form-btn" @click="loginFn">
                        <div class="login100-form-bgbtn"></div>
                        <button class="login100-form-btn">
                            登录
                        </button>
                    </div>
                </div>

                <div class="text-center">
                    <span class="txt1">
                        还木有账号？
                    </span>

                    <a class="txt2" href="#" @click="showRegBoxFn">
                        注册
                    </a>
                </div>
            </div>
        </div>
        <el-dialog
          title="快速注册"
          :visible.sync="showRegBox"
          @close='closeDialog'
          width="70%"
          :modal-append-to-body='false'
          :show-close='false'>
            <div class="input-wrapper">
                <div class="wrap-input100 validate-input" ref="user-dom" data-validate = "账号至少为4位且为数字或字母">
                    <input v-model="usernameText" class="input100" @focus="focusHandle('user', $event)" @blur="blurHandle('user', $event)" type="text" maxlength="16">
                    <span class="focus-input100" data-placeholder="账号"></span>
                </div>

                <div class="wrap-input100 validate-input" ref="nick-dom" data-validate = "昵称至少为4位">
                    <input v-model="nicknameText" class="input100" @focus="focusHandle('nick', $event)" @blur="blurHandle('nick', $event)" type="text" maxlength="16">
                    <span class="focus-input100" data-placeholder="昵称"></span>
                </div>

                <div class="wrap-input100 validate-input" ref="pwd-dom" data-validate="密码至少为6位">
                    <input v-model="pwdText" class="input100" @focus="focusHandle('pwd', $event)" @blur="blurHandle('pwd', $event)" type="password" name="pass" maxlength="20">
                    <span class="focus-input100" data-placeholder="密码"></span>
                </div>

                <div class="wrap-input100 validate-input" ref="sign-dom">
                    <input v-model="signText" class="input100" @focus="focusHandle('sign', $event)" @blur="blurHandle('sign', $event)" type="text" name="sign" maxlength="20">
                    <span class="focus-input100" data-placeholder="个性签名"></span>
                </div>
                <el-form class="form-text">
                  <el-form-item label="">
                    <el-radio-group v-model="sex">
                      <el-radio label="0">女</el-radio>
                      <el-radio label="1">男</el-radio>
                      <el-radio label="-1">保密</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-form>
                <div class="container-login100-form-btn">
                    <div class="wrap-login100-form-btn" @click="regFn">
                        <div class="login100-form-bgbtn"></div>
                        <button class="login100-form-btn">
                            注册
                        </button>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
</template>
<script type="text/javascript">
import Common from '../assets/scripts/common.js'
import $axios from 'axios';
import Cookies from "js-cookie";
export default {
    name: "Login",
    // mounted () {
    //     Common.canvas("starts",230,1000,60,2,50000,0.5);
    // }
    data () {
      return {
        showRegBox: false,
        username: "",
        pwd: "",
        nickname: "",
        sign: "",
        usernameText: "",
        pwdText: "",
        nicknameText: "",
        signText: "",
        sex: '-1'
      };
    },
    methods: {
        closeDialog: function () {
            this.showRegBox = false;
        },
        showRegBoxFn: function () {
            this.showRegBox = true;
        },
        blurHandle: function (dom, event) {
            var $target = $(event.target);
            var regUser = /^[a-zA-Z0-9_-]{4,16}$/;
            var regPwd = /^[a-zA-Z0-9_-]{6,20}$/;
            var regNick = /^[a-zA-Z\d\u4e00-\u9fa5]$/;
            // var mailReg = /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/;
            var val = $target.val();
            var $dom = $(this.$refs[dom + '-dom']);
            var that = this;
            if (val != '') {
                $target.addClass('has-val');
            } else {
                $target.removeClass('has-val');
            }
            if (dom == 'user' && !regUser.test(val)) {
                $dom.addClass('alert-validate');
            } else if (dom == 'user' && regUser.test(val)) {
                that.username = val;
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

            if (dom == 'sign' && val.length > 20) {
                $dom.addClass('alert-validate');
            } else if (dom == 'sign' && val.length <= 20) {
                that.sign = val;
                $dom.removeClass('alert-validate');
            }
        },
        focusHandle: function (dom, event) {
            var $target = $(event.target);
            var $dom = $(this.$refs[dom + '-dom']);
            $dom.removeClass('alert-validate');
        },
        loginFn: function () {
            let that = this;
            if (that.username != '' && that.pwd != '') {
                $axios.post('/api/login', {
                    "username": (that.username).replace(/\s/g, ''),
                    "password": (that.pwd).replace(/\s/g, ''),
                }).then((res) => {
                    if (res && res.data && !res.data.code) {
                        that.$router.push('/index');
                    } else {
                         that.$message({
                            message: res.data.message,
                            type: 'warning'
                        });
                        return false;
                    }
                });
            }
        },

        regFn: function () {
            let that = this;
            if (that.username != '' && that.pwd != '' && that.nickname != '') {
                $axios.post('/api/registe', {
                    "username": (that.username).replace(/\s/g, ''),
                    "password": (that.pwd).replace(/\s/g, ''),
                    "nickname": (that.nickname).replace(/\s/g, ''),
                    "sex": that.sex,
                    "sign": (that.sign).replace(/\s/g, '') || ''
                }).then((res) => {
                    if (res && res.data && !res.data.code) {
                        that.$message({
                            message: '注册成功,请登录~',
                            type: 'success'
                        });
                        that.resetForm();
                        that.showRegBox = false;

                    } else {
                        that.$message({
                            message: res.data.message,
                            type: 'warning'
                        });
                        return false;
                    }
                });
            }
        },

        resetForm: function () {
          let that = this;
          // let refs = {
          //   'user': 'user-dom',
          //   'nick': 'nick-dom',
          //   'pwd': 'pwd-dom',
          //   'sign': 'sign-dom'
          // };
          $(that.$refs['user-dom']).find('input').removeClass('has-val');
          $(that.$refs['nick-dom']).find('input').removeClass('has-val');
          $(that.$refs['pwd-dom']).find('input').removeClass('has-val');
          $(that.$refs['sign-dom']).find('input').removeClass('has-val');
          that.usernameText = "";
          that.nicknameText = "";
          that.pwdText = "";
          that.signText = "";
          that.sex = "-1";
          that.username = "";
          that.pwd = "";
          that.nickname = "";
          that.sign = "";


        }
    }
}
</script>
<style type="text/css">
    /*注册相关*/
    .input-wrapper {
        width: 50%;
        margin: 0.1rem auto;
    }


    .container-login100 {
      width: 100%;
      min-height: 100vh;
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      align-items: center;
      padding: 0.15rem;
    }

    .wrap-login100 {
      width: 4rem;
      background: #fff;
      border-radius: 0.1rem;
      overflow: hidden;
      padding: 0.75rem 0.55rem 0.35rem 0.55rem;
      box-shadow: 0 0.05rem 0.1rem 0 rgba(0, 0, 0, 0.1);
    }


    /*------------------------------------------------------------------
    [ Form ]*/
    .form-text {
      text-align: center;
    }
    .wrap-login100-form-btn {
        width: 100%;
        display: block;
        position: relative;
        z-index: 1;
        border-radius: 25px;
        overflow: hidden;
        margin: 0 auto;
    }
    .login100-form {
      width: 100%;
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
      position: absolute;
      display: block;
      width: 100%;
      height: 100%;
      top: 0;
      left: 0;
      pointer-events: none;
      text-align: center;
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