#  CTF_Lottery解题思路

## ECB算法的特点
```json
{"lottery":"2421f918-5926-4ecc-cafc-cba0171e5fbe","user":"fff805af-3316-4ee2-adcb-2d4f830d29ae","coin":6}
```
长度105，加密后128字节
RIJNDAEL_256以32字节为分组单位（可以验证33字节明文加密后为64字节）
加密后总共128字节。以32字节作为分组。

```txt
{"lottery":"1d305db5-1982-59ef-5  
95e-35ef40cc4bcb","user":"2e8a05
af-3316-4ee2-adcb-2d4f830d29ae",
"coin":6}
```
第二段包括了lottery和user，它们被作为一个整体。因此：如果借用其他用户的彩票 ，要借前2段，导致user不符合。有办法能把第2段后面6个字节改成自己想要的吗？

## php json_decode遇到两个同名的属性会怎样
测试json中有两个同名属性时，最后取到的是最后一个
```php
$me = '{"lottery":"4c740172-0a3d-7486-4ac1-9acaf1bf1333","user":"fff80595e-35ef40cc4bcb","user":"2e8a05af-3316-4ee2-adcb-2d4f830d29ae","coin":6}';
$info = json_decode($me);
echo $info->user  . "\r\n";
```

## 解法
https://blog.csdn.net/weixin_43859686/article/details/112830894
解法：be(i[:64] + ii[32:]) 其他人的enc取0到64，自己的enc取32到96，两者拼起来。有两个user字段，会取后面那个作为值。java有没有这种特性？

