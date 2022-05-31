package com.qunshang.wenpaitong.equnshang.utils

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    companion object{

        var currentTime = 0

        var isVideoThreadRunning = false

        var isWatchingPaper = false

        var currentPaperTime = 0

        val timer = Timer()

        val timerTask = object : TimerTask(){
            override fun run() {
                if (isVideoThreadRunning){
                    currentTime++
                    StringUtils.log("当前观看视频的时间" + currentTime)
                    //Log.i(Constants.logtag,"当前TImer" + currentTime)
                    if (currentTime % 60 == 0){
                        if (!StringUtils.isEmpty(UserInfoViewModel.getUserId())){
                            //Log.i(Constants.logtag,"我掉了啊 ")
                            ApiManager.getInstance().getApiVideoTest().watchVideoToBrowse(UserInfoViewModel.getUserId()).enqueue(object : Callback<ResponseBody>{
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    if (response.body() == null){
                                        return
                                    }
                                    Log.i(Constants.logtag,response.body()!!.string())
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                                }

                            })
                        }
                    }
                }
                if (isWatchingPaper){
                    currentPaperTime++
                    StringUtils.log("当前浏览文章的时间是" + currentPaperTime)
                    if (currentPaperTime % 60 == 0){
                        CommonUtil.doCompleteTask(2)
                    }
                }
            }
        }

        fun startPaper(){
            isWatchingPaper = true
        }

        fun pausePaper(){
            isWatchingPaper = false
        }

        fun startComputeTimer(){
            try {
                timer.schedule(timerTask,0,1000)
            } catch (e : Exception){
                e.printStackTrace()
            }
            isVideoThreadRunning = true
        }

        fun pauseCompute(){
            isVideoThreadRunning = false
        }

        fun getDateByString(time: String?): Date? {
            var date: Date? = null
            val format_string = "yyyy-MM-dd HH:mm:ss"
            val format = SimpleDateFormat(format_string)
            try {
                date = format.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date
        }

        fun getTimeRemainByDayString(endtime: String?) : String{
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            //var startTime: Date = dateFormat.parse("2018-01-01 12:10:10")
            //var endTime: Date = dateFormat.parse("2018-01-01 14:10:10")

            val diff = (dateFormat).parse(endtime).time - System.currentTimeMillis()
            if (diff <= 0){
                return "00:00:00"
            }
            /*if (test){
                return "00:00:00"
            }*/
            var days = diff / (1000 * 60 * 60 * 24)
            val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
            val second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000
            return (days*24 + hours).toString() + ":" + StringUtils.addZeroForNum(minutes.toString(),2) + ":" + StringUtils.addZeroForNum(second.toString(),2)
        }

        fun getTimeRemainByDayStringWithDay(endtime: String?,test: Boolean = false) : String{
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            //var startTime: Date = dateFormat.parse("2018-01-01 12:10:10")
            //var endTime: Date = dateFormat.parse("2018-01-01 14:10:10")


            val diff = (dateFormat).parse(endtime).time - System.currentTimeMillis()
            if (diff <= 0){
                return "00:00:00"
            }
            /*if (test){
                return "00:00:00"
            }*/
            var days = diff / (1000 * 60 * 60 * 24)
            val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
            val second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000
            return days.toString() + "天" + hours + "时" + minutes + "分"
        }

        fun rollMinute(d: Date, minute: Int): Date? {
            return Date(d.time + minute * 60 * 1000)
        }

        /**
         * 前/后?天
         *
         * @param d
         * @param day
         * @return
         */
        fun rollDay(d: Date?, day: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = d
            cal.add(Calendar.DAY_OF_MONTH, day)
            return cal.time
        }

        /**
         * 前/后?月
         *
         * @param d
         * @param mon
         * @return
         */
        fun rollMon(d: Date?, mon: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = d
            cal.add(Calendar.MONTH, mon)
            return cal.time
        }

        /**
         * 前/后?年
         *
         * @param d
         * @param year
         * @return
         */
        fun rollYear(d: Date?, year: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = d
            cal.add(Calendar.YEAR, year)
            return cal.time
        }

        fun rollDate(d: Date?, year: Int, mon: Int, day: Int): Date? {
            val cal = Calendar.getInstance()
            cal.time = d
            cal.add(Calendar.YEAR, year)
            cal.add(Calendar.MONTH, mon)
            cal.add(Calendar.DAY_OF_MONTH, day)
            return cal.time
        }

        fun getLastLotteryRemainTIme(time : String) : String{
            var splittime = time.split(" ")
            var timeofzero : String
            if (splittime.size == 2){
                timeofzero = splittime[0] + " " + "00:00:00"//获取获取日期当天0点的日期
                Log.i(Constants.logtag,"但提前的瞬间是" + timeofzero)
            } else {
                return "00:00:00"
            }
            val end = rollDay(getDateByString(timeofzero),7)



            val format_string = "yyyy-MM-dd HH:mm:ss"
            val format = SimpleDateFormat(format_string)
            val formatedentime = format.format(end)
            Log.i(Constants.logtag,"时间是" + formatedentime)

            return getTimeRemainByDayString(formatedentime)

            Log.i(Constants.logtag,"" + end!!.year + "年" + end!!.month + "月" + end!!.day + "天" + end.hours + "使" + end.minutes + "分" + end.seconds)
            val currentDate = Date()
            if (end!!.time < currentDate.time){
                return "00:00:00"
            }
            var remainHours = end!!.hours - currentDate.hours
            var remainMinutes = end.minutes - currentDate.minutes
            if (remainMinutes < 0){
                remainHours--
                remainMinutes += 60
            }
            var remainSeconds = end.seconds - currentDate.seconds
            if (remainSeconds < 0){
                remainMinutes --
                remainSeconds += 60
            }
            return StringUtils.addZeroForNum(remainHours.toString(),2) + ":" + StringUtils.addZeroForNum(remainMinutes.toString(),2) + ":" + StringUtils.addZeroForNum(remainSeconds.toString(),2)
        }

        //根据提供的日期，获取剩余的时间
        fun getRemainTimeString(endtime : String) : String{//6:0:0   3:0:1
            /*val end = getDateByString(endtime)
            val currentDate = Date()
            if (end!!.time < currentDate.time){
                return "00:00:00"
            }
            var remainHours = end!!.hours - currentDate.hours
            var remainMinutes = end.minutes - currentDate.minutes
            if (remainMinutes < 0){
                remainHours--
                remainMinutes += 60
            }
            var remainSeconds = end.seconds - currentDate.seconds
            if (remainSeconds < 0){
                remainMinutes --
                remainSeconds += 60
            }*/
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            //var startTime: Date = dateFormat.parse("2018-01-01 12:10:10")
            //var endTime: Date = dateFormat.parse("2018-01-01 14:10:10")


            val diff = (dateFormat).parse(endtime).time - System.currentTimeMillis()
            if (diff <= 0){
                return "00:00:00"
            }
            var days = diff / (1000 * 60 * 60 * 24)
            val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
            val second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000
            return StringUtils.addZeroForNum(((days*24 + hours).toString()),2) + ":" + StringUtils.addZeroForNum(minutes.toString(),2) + ":" + StringUtils.addZeroForNum(second.toString(),2)

            //return StringUtils.addZeroForNum(remainHours.toString(),2) + ":" + StringUtils.addZeroForNum(remainMinutes.toString(),2) + ":" + StringUtils.addZeroForNum(remainSeconds.toString(),2)
        }


        fun getDayOfWeek() : Int {
            val c = Calendar.getInstance()
            c.timeZone = TimeZone.getTimeZone("GMT+8:00")
            var mWay = c[Calendar.DAY_OF_WEEK]
            if (1 == mWay) {
                mWay = 7
            } else if (2 == mWay) {
                mWay = 1
            } else if (3 == mWay) {
                mWay = 2
            } else if (4 == mWay) {
                mWay = 3
            } else if (5 == mWay) {
                mWay = 4
            } else if (6 == mWay) {
                mWay = 5
            } else if (7 == mWay) {
                mWay = 6
            }
            return mWay
        }

        fun getCurrentHour() : Int{
            val calendar = Calendar.getInstance()
            val curHour24 = calendar.get(Calendar.HOUR_OF_DAY)
            return curHour24
        }


    }

}