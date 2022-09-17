import axios from 'axios'

class HelloWorldService {

  executeHelloWorldService() {
    console.log('executed service')
    //return axios.get('http://localhost:8080/api/employees/1');
    return axios.get('http://localhost:8080/api/employees');
    //return axios.get('http://localhost:8080/');
    //http://localhost:8080/api/employees
  }
}

export default new HelloWorldService()
