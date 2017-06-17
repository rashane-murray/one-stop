import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import { LoginPage } from '../login/login';
import { SignupPage } from '../signup/signup';
//import { GoogleAuth, User } from '@ionic/cloud-angular';
import { MainPage } from '../../pages/pages';

/**
* 	The Welcome Page is a splash page that quickly describes the app, 
* 	and then directs the user to create an account or log in.
*/

@Component({
  selector: 'page-welcome',
  templateUrl: 'welcome.html'
})

export class WelcomePage {

  constructor(public navCtrl: NavController) { }

  loginFb() {
  
  	this.navCtrl.push(MainPage);
    
  }

    loginGoogle() {
  	/*this.googleAuth.login().then((resp)=>{
  		this.navCtrl.push(MainPage);
  	}, (err)=>{
  		this.navCtrl.push(MainPage);
  	})*/

  	this.navCtrl.push(MainPage);
    
  }

  signUp() {
    this.navCtrl.push(SignupPage);
  }

  signIn() {
    this.navCtrl.push(LoginPage);
  }

}
