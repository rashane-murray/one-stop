import { Component } from '@angular/core';
import { NavController, ToastController } from 'ionic-angular';

import { MainPage } from '../../pages/pages';
import { User } from '../../providers/user';
import { TranslateService } from '@ngx-translate/core';
import { Http, Headers } from "@angular/http";
import { UserAccount } from '../../models/UserAccount';
import { AngularFireAuth } from "angularfire2/auth";

@Component({
  selector: 'page-signup',
  templateUrl: 'signup.html'
})
export class SignupPage {

  /** The account fields for the login form.
  *   If you're using the username field with or without email, make
  *   sure to add it to the type
  */
  
  // Our translated text strings
  private signupErrorString: string;
  posts:any;
  account: { username: string, fname: string,lname: string, email: string, password: string , phone: string};
  useracc = {} as UserAccount;
  constructor(public http: Http,public navCtrl: NavController,
    public user: User,
    public toastCtrl: ToastController,
    public translateService: TranslateService,
    private afAuth : AngularFireAuth) {

    this.translateService.get('SIGNUP_ERROR').subscribe((value) => {
      this.signupErrorString = value;
    })
  };

  

  
  doSignup() {
   let headers = new Headers();
    headers.append("content-type", "application/json");
    let info = {
      name: "oneStop",
      fname: this.account.fname,
      lname: this.account.lname,
      email: this.account.email,
      password: this.account.password,
      phone: this.account.phone,
      utc_timestamp: "14000000"
    };

    this.http
      .post(
        "http://localhost:3000/mdl/api/v1/mobile/post/register/rider",
        JSON.stringify(info),
        { headers: headers }
      )
      .subscribe(
        data => {
          this.posts = data;
        },
        err => {
      
        this.navCtrl.push(MainPage); // TODO: Remove this when you add your signup endpoint

        // Unable to sign up
        let toast = this.toastCtrl.create({
        message: this.signupErrorString,
        duration: 3000,
        position: 'top' 
      });
      toast.present();
    });
  }

  async signUp(useracc: UserAccount){
    try{
      const result = await this.afAuth.auth.createUserWithEmailAndPassword(useracc.email, useracc.password);
      if(result){
        this.navCtrl.push(MainPage);
      }
    }catch(e){
      console.error(e);
    }
  }
}
