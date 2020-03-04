package com.example.pockemonandroid

import android.location.Location
import android.media.Image

class Pockemon{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null

    var isCaught:Boolean=false
    var locationOfPock:Location?=null
    constructor(des:String,image:Int,latitude:Double,longitude:Double,name:String,power:Double){
        this.name=name
        this.des=des
        this.image=image
        this.power=power
        this.locationOfPock=Location(this.name)
        this.locationOfPock!!.latitude=latitude
        this.locationOfPock!!.longitude=longitude
        this.isCaught=false

    }
}