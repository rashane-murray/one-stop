import { Component, ViewChild,ElementRef } from '@angular/core';
import { NavController, Platform } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';

import {  GoogleMaps, GoogleMap, GoogleMapsEvent, LatLng, CameraPosition, MarkerOptions, Marker } from '@ionic-native/google-maps';

declare var google: any;
declare var navigaton:any;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})
export class MapPage {
  
  @ViewChild('map') mapEle;
  map:any;
  position:any;
  marker:any;
  content:any;
  latLng:any;

  constructor(private googleMaps: GoogleMaps, public navCtrl: NavController, public platform: Platform, public geolocation: Geolocation) { }

  ionViewDidLoad(){
    this.loadMap();

  }

  loadMap() {

    /*let mapEle = this.map.nativeElement;

        if (!mapEle) {
            console.error('Unable to initialize map, no map element with #map view reference.');
            return;
        }*/

    //let TAXI: LatLng = ...
    this.geolocation.getCurrentPosition().then((position)=>{
        this.latLng = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);

        let mapOptions = {
            center: this.latLng, //TAXI
            zoom: 15,
            mapTypeId:google.maps.MapTypeId.ROADMAP

        }

    this.map=new google.maps.Map(this.mapEle.nativeElement,mapOptions);
    },(err) =>{
        console.log(err);
        });
    
    this.addMarker();   
    }

    addMarker(){
 
        let marker = new google.maps.Marker({
            map: this.map,
            animation: google.maps.Animation.DROP,
            position: this.latLng
        });
 
        let content = "<h4>TAXI?</h4>";          
        this.addInfoWindow(marker, content);
    } 

    addInfoWindow(marker, content){
 
        let infoWindow = new google.maps.InfoWindow({
            content: content
        });
 
        infoWindow.open(this.map, marker);
    }
}


