<template>
  <div style="width: 100%;">
    <Header/>
    <div class="container" style="padding-top: 150px; padding-bottom: 50px;">
      <router-view></router-view>
      <ErrorModal />
    </div>
  </div>
</template>

<script>
import Header from './components/Header.vue';
import ErrorModal from "@/components/error/ErrorModal.vue";

export default {
  name: 'App',
  components: {
    ErrorModal,
    Header
  },
};
</script>

<style>
body, html {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
}
.container{
  max-width: 50%;
  margin: 0 auto;
  padding: 0 20px;
}
</style>
