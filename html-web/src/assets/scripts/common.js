import $axios from 'axios';
import Qs from 'qs';
import urlList from './urlList.js';
function formatTime (time) {
    let tempTime;
    time ? (tempTime = new Date(parseInt(time, 10))) : (tempTime= new Date());
    return tempTime.getFullYear() + "-"
    + (tempTime.getMonth() + 1) + "-"
    + tempTime.getDate() + " "
    + (tempTime.getHours() < 10 ? "0" + tempTime.getHours() : tempTime.getHours()) + ":"
    + (tempTime.getMinutes() < 10 ? "0" + tempTime.getMinutes() : tempTime.getMinutes()) + ":"
    + (tempTime.getSeconds() < 10 ? "0" + tempTime.getSeconds() : tempTime.getSeconds());
}

function trimString (value) {
    return (value || '').replace(/\s/g, '');
}

function axios (options) {
  let url = urlList[options.url] || "";
  let method = options.method || 'get';
  let data = options.data || {};
  if (!url) {
    return;
  }

  if (method == 'post') {
    data = QS.stringify(data);
  }

  return new Promise((resolve, reject) => {
    $axios[method](url, {
        params: data
    }).then(res => {
        resolve(res.data);
    }).catch(err =>{
        reject(err.data)
    })
  })

}

export default {formatTime, trimString, axios};