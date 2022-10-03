//import axios from 'axios';    
//
//// Add a request interceptor
//axios.interceptors.request.use(
//  function (config) {
//    // Do something before request is sent
//    config.headers.Authorization = `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb20uam9uYXRoYW4ud2ViIiwic3ViIjoidGVzdF91c2VyMSIsImV4cCI6MTY2NDc2ODY2NH0.rjEsQLBec-vSVxhmqbT-No5a6Yqmm6OlyGreqDVX7HXtDXqGV_qiogJcRgABU-gt-Rav4skTp5suJQSDDOqiuQ`;
//    // OR config.headers.common['Authorization'] = `Bearer ${your_token}`;
//    //config.baseURL = 'http://localhost:8080/';
//
//    return config;
//  },
//  function (error) {
//    // Do something with request error
//    return Promise.reject(error);
//  }
//);
//
//export default {
//  get: axios.get,
//  post: axios.post,
//  put: axios.put,
//  delete: axios.delete,
//  patch: axios.patch
//};
