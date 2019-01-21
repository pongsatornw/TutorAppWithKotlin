# TutorAppWithKotlin
แอปพลิเคชันสำหรับการลงเวลาเข้าชั้นเรียนของสถาบันกวดวิชา มีคุณสมบัติดังนี้ 
- มีการใช้ FirebaseFirestore เป็นฐานข้อมูล
- มีการใช้ Service ในการอัพเดท หรือ เพิ่มข้อมูลใน FirebaseFirestore
- มี 2 Activity คือ MainActivity และ LoginActivity ส่วนที่เหลือใช้ FragmentTransition
- ใช้ Font THSaraban New ในการแสดงผล text
- มีการจำลอง QRCode เพื่อให้นักเรียนใช้ในการลงเวลาเรียนได้
- มีการส่งข้อมูลระหว่าง Fragment โดยใช้ @Parcelize และ implement Parcelable
- ในการ Query ข้อมูล มีการเขียน CallBack

*** ได้ทำการลบ google-service.json แล้ว ***
