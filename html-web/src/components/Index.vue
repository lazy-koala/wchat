<template>
    <div class="wrapper">
        <div class="sidebar">
            <info></info>
            <list></list>
        </div>
        <div class="main">
            <message></message>
            <text-area></text-area>
        </div>
    </div>
</template>

<script>
import List from './indexItem/List';
import Info from './indexItem/Info';
import TextArea from './indexItem/TextArea';
import Message from './indexItem/Message';

export default {
  name: 'Index',
  components: {
    List,
    Info,
    TextArea,
    Message
  },
  methods: {
  },

}
</script>
<!-- <script type="text/javascript" src="../assets/scripts/ws.js"></script> -->

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss">
.wrapper {
    margin: 0.5rem auto;
    width: 8rem;
    height: 6rem;

    overflow: hidden;
    border-radius: 0.03rem;

    .sidebar, .main {
        height: 100%;
    }
    .sidebar {
        float: left;
        width: 2rem;
        color: #f4f4f4;
        background-color: #2e3238;
    }
    .main {
        position: relative;
        overflow: hidden;
        background-color: #eee;
    }
}
.el-dialog__body {
      padding: 0.1rem 0.2rem;
  }
</style>
