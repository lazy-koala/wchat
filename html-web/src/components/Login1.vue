<template>
    <div class='login-wrapper'>
        <div class='login_title'>
            <span>登录</span>
        </div>
        <div class='login_fields'>
            <div class='login_fields__user'>
              <div class="login_files_text">
                <span ref="user" class="icon iconfont icon-user"></span>
                <input name="login" @blur="blurIcon('user', $event)" @focus="focusIcon('user')" placeholder='用户名' maxlength="16" class="" type='text' autocomplete="off"/>
              </div>
              <div ref="user_check" class="dn icon iconfont icon-duihao">
              </div>
            </div>
            <div class='login_fields__user'>
              <div class="login_files_text">
                <span ref="pwd" class="icon iconfont icon-pwd"></span>
                <input name="pwd" @blur="blurIcon('pwd', $event)" @focus="focusIcon('pwd')" placeholder='密码' maxlength="16" class="" type='password' autocomplete="off"/>
              </div>
              <div ref="pwd_check" class="dn icon iconfont icon-duihao">
              </div>
            </div>
            <div class='login_fields__submit'>
                <input type='button' @click="submit" value='登录'>
            </div>
        </div>
        <div class='success'>
        </div>
    </div>

    <!-- </div> -->
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
    user: "",
    pwd: ""

  };
},

methods: {
  blurIcon: function (dom, event) {
    var $dom = $(this.$refs[dom]);
    var $domCheck = $(this.$refs[dom + '_check'])
    var val = $(event.target).val();
    var regUser = /^[a-zA-Z0-9_-]{4,16}$/;
    var regPwd = /^[a-zA-Z0-9_-]{6,20}$/;
    if (dom == 'user' && !regUser.test(val)) {
      $domCheck.addClass('dn');
      this.$message.error('用户名为数字或者字母，长度为4~16个字符');
    } else if (dom == 'user' && regUser.test(val)) {
      this.user = val;
      $domCheck.removeClass('dn');
    }
    if (dom == 'pwd' && !regPwd.test(val)) {
      $domCheck.addClass('dn');
      this.$message.error('密码为数字或者字母，长度为6~20个字符');
    } else if (dom == 'pwd' && regPwd.test(val)) {
      this.pwd = val;
      $domCheck.removeClass('dn');
    }
    $dom.removeClass('blurStyle');
  },
  focusIcon: function (dom) {
    var $domCheck = $(this.$refs[dom + '_check'])
    $domCheck.addClass('dn');
    $(this.$refs[dom]).addClass('blurStyle');
  },
  submit: function () {
    let that = this;
    if (that.user != '' && that.pwd != '') {
        $axios.post('/api/login', {
            "username": (that.user).replace(/\s/g, ''),
            "password": (that.pwd).replace(/\s/g, ''),
        }).then((res) => {
            if (res && res.data && !res.data.code) {
                that.$router.push('/index');
            } else {
                 Message.error({
                    message: res.data.message,
                    center: true
                });
                return false;
            }
        });
    }
  }
}
}
</script>

<style type="text/css" scoped>
    .login-wrapper{
      position: relative;
      width: 3rem;
      height: 2rem;
      text-align: center;
      margin: 2rem auto;
      box-shadow: -0.15rem 0.15rem 0.15rem rgba(6, 17, 47, 0.7);
      opacity: 1;
      -webkit-transition-timing-function: cubic-bezier(0.68, -0.25, 0.265, 0.85);
      transition-property: transform,opacity,box-shadow,top,left;
      transition-duration: .5s;
      transform-origin: 1.6rem 100%;
      transform: rotateX(0deg);
      padding: 0.5rem 0.4rem 0.4rem 0.4rem;
      background: #35394a;
      border-radius: 0.05rem;
      /*background: linear-gradient(230deg, rgba(53, 57, 74, 0) 0%, rgb(0, 0, 0) 100%);*/
    }

    .login_title {
      width: 3rem;
      font-size: 0.36rem;
      font-weight: 800;
      margin-bottom: 0.2rem;
      animation: masked-animation 1.5s ease-in-out infinite alternate;
    }

    @keyframes masked-animation {
      from {
        text-shadow: 0 0 5px #eee,
                   0 0 15px  #eee,
                   0 0 20px  #35394a,
                   0 0 25px  #228DFF,
                   0 0 30px  #228DFF,
                   0 0 35px #228DFF,
                   0 0 40px #228DFF;
      }
      to {
        text-shadow: 0 0 5px #eee,
                   0 0 15px #fff,
                   0 0 20px #35394a,
                   0 0 35px #228DFF,
                   0 0 40px #228DFF;
      }
    }

    /*.login .disclaimer {
      position: absolute;
      bottom: 20px;
      left: 35px;
      width: 250px;
    }*/

    .login_fields input[type='password'],.login_fields input[type='text'] {
      color: #61BFFF !important;
    }
    .login_fields input[type='text'], .login_fields input[type='password'] {
      color: #afb1be;
      font-size: 16px;
      width: 1.9rem;
      padding-left: 0.1rem;
      background-color: transparent;
      /*background: rgba(57, 61, 82, 0);*/
      border-top: 2px solid rgba(57, 61, 82, 0);
      border-bottom: 2px solid rgba(57, 61, 82, 0);
      border-right: none;
      border-left: none;
      outline: none;
      font-family: 'Gudea', sans-serif;
      box-shadow: none;
    }

    .login_files_text {
      flex: 1;
      display: flex;
      border-bottom: 2px solid rgba(57, 61, 82, 1);

    }


    .login_fields__user {
        display: flex;
        line-height: 0.35rem;
        margin-top: 0.15rem;
        position: relative;
    }

    .login_fields__user .icon {
      opacity: 0.3;
      font-size: 24px;
      color: #fff;
    }

    .login_fields__user  .icon-duihao {
      color: red;
    }

    .login_fields__user .blurStyle {
      opacity: 1

    }

    input:-webkit-autofill {
      background-color: transparent;
      background-image: none;
      color: #000;
    }

    .dn {
      display: none;
    }

    .login_fields__submit {
      position: relative;
      top: 0.2rem;
      left: 0;
      width: 80%;
      right: 0;
      margin: auto;
    }

    .login_fields__submit input {
      border-radius: 50px;
      background: transparent;
      padding: 10px 50px;
      border: 2px solid #4FA1D9;
      color: #4FA1D9;
      text-transform: uppercase;
      font-size: 16px;
      transition-property: background,color;
      transition-duration: .2s;
    }
    .login_fields__submit input:focus {
      box-shadow: none;
      outline: none;
    }
    .login_fields__submit input:hover {
      color: white;
      background: #4FA1D9;
      cursor: pointer;
      transition-property: background,color;
      transition-duration: .2s;
    }

</style>