import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router";
import axios from "axios";
import 'element-plus/theme-chalk/dark/css-vars.css';
import 'bootswatch/dist/lux/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import 'bootstrap-icons/font/bootstrap-icons.css';



axios.defaults.baseURL = 'http://localhost:8080'
export const baseUrl = 'http://localhost:8080'

const app = createApp(App)

app.use(router)
app.mount('#app')