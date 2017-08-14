import { Component, ViewChild,ElementRef } from '@angular/core';
import { NavController, Platform } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { Items } from '../../providers/providers';
import { Item } from '../../models/item';
import {  GoogleMaps, GoogleMap, GoogleMapsEvent, LatLng, CameraPosition, MarkerOptions, Marker } from '@ionic-native/google-maps';

declare var google: any;
declare var navigaton:any;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})
export class MapPage {
  
  @ViewChild('map') mapEle;
  @ViewChild('directionsPanel') directionsPanel;
  map:any;
  position:any;
  marker:any;
  content:any;
  latLng:any;

  constructor(private googleMaps: GoogleMaps, public navCtrl: NavController, public platform: Platform, public geolocation: Geolocation) { }

  ionViewDidLoad(){
    this.loadMap();
    this.addMarker();
    this.startNavigating();

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
 
        let content = "<h4>Location</h4>";          
        this.addInfoWindow(marker, content);
    } 

    addInfoWindow(marker, content){
 
        let infoWindow = new google.maps.InfoWindow({
            content: content
        });
 
        infoWindow.open(this.map, marker);
    }

    startNavigating(){
    let directionsService = new google.maps.DirectionsService;
        let directionsDisplay = new google.maps.DirectionsRenderer;
 
        directionsDisplay.setMap(this.map);
        directionsDisplay.setPanel(this.directionsPanel.nativeElement);
 
        directionsService.route({
            origin: {lat: 37.77, lng: -122.447},
destination: {lat: 37.768, lng: -122.511},
            travelMode: google.maps.TravelMode['DRIVING']
        }, (res, status) => {
 
            if(status == google.maps.DirectionsStatus.OK){
                directionsDisplay.setDirections(res);
            } else {
                console.warn(status);
            }
 
        });
 
    }

    }



