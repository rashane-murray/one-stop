import { Component } from '@angular/core';
import { NavController, ToastController } from 'ionic-angular';

import { MainPage } from '../../pages/pages';
import { User } from '../../providers/user';
import { TranslateService } from '@ngx-translate/core';
import { Http, Headers } from "@angular/http";
import { AngularFireAuth } from "angularfire2/auth";
import { UserAccount } from "../../models/UserAccount";
//import { GoogleAuth, User } from '@ionic/cloud-angular';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {
  /* The account fields for the login form.
   If you're using the username field with or without email, make
   sure to add it to the type
   */ 
    posts:any;
    account: { email: string, password: string } = {
    email: 'test@example.com',
    password: 'test'
  }; 
  useracc = {} as UserAccount;

  //Our translated text strings
  private loginErrorString: string;

  constructor(public http: Http,
    public navCtrl: NavController,
    public user: User,
    public toastCtrl: ToastController,
    public translateService: TranslateService,
    private afAuth: AngularFireAuth) {

    this.translateService.get('LOGIN_ERROR').subscribe((value) => {
      this.loginErrorString = value;
    })
  }

  //User authentication
  doLogin() {

      let headers = new Headers();
      headers.append("content-type", "application/json");
      let log = { name: "oneStop", email: this.account.email, password: this.account.password};
      this.http.post("http://localhost:3000/mdl/api/v1/mobile/post/login/rider",
        JSON.stringify(log),
        { headers: headers }
      )
      .subscribe(
        data => {
        this.posts = data;
        
      },
      err=>{
          
      let toast = this.toastCtrl.create({
        message: this.loginErrorString,
        duration: 3000, 
        position: 'top'
      });
      toast.present(); 
    
  });
  this.navCtrl.push(MainPage);
}
  async login(useracc: UserAccount){
    try{
      const result = this.afAuth.auth.signInWithEmailAndPassword(useracc.email, useracc.password);
      if(result){
        this.navCtrl.push(MainPage);
      }
    }catch(e){
      console.error(e);
    }
  }
  
}