<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Models\Lottery;
use Illuminate\Http\Request;
use App\Exceptions\Exception;
use Laravel\Lumen\Routing\Controller as BaseController;

class LotteryController extends BaseController
{
    protected $price = 100;

    public function buy(Request $request)
    {
        $user = $request->user();
        if ($user->coin < $this->price) {
            throw new Exception("no enough coin");
        }

        $cnt = User::where('id', $user->id)->where('coin', $user->coin)
            ->decrement('coin', $this->price);
        if ($cnt === 0) {
            throw new Exception("unknown error");
        }

        $lottery = Lottery::create(['coin' => 100 - floor(sqrt(random_int(1, 10000)))]);
        $serilized = json_encode([
            'lottery' => $lottery->uuid,
            'user' => $user->uuid,
            'coin' => $lottery->coin,
        ]);
        // {"lottery":"2421f918-5926-4ecc-cafc-cba0171e5fbe","user":"fff805af-3316-4ee2-adcb-2d4f830d29ae","coin":6}
        // 长度105，加密后128字节
        // RIJNDAEL_256以32字节为分组单位（可以验证33字节明文加密后为64字节）
        // 加密后总共128字节。以32字节作为分组。
        // {"lottery":"1d305db5-1982-59ef-5  
        // 95e-35ef40cc4bcb","user":"2e8a05
        // af-3316-4ee2-adcb-2d4f830d29ae",
        // "coin":6}
        // 第二段包括了lottery和user，它们被作为一个整体。因此：如果借用其他用户的彩票 ，要借前2段，导致user不符合。有办法能把第2段后面6个字节改成自己想要的吗？
        $enc = base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_256, env('LOTTERY_KEY'), $serilized, MCRYPT_MODE_ECB));
        return [
            'enc' => $enc,
            // 'serialized' => $serilized,
        ];
    }

    public function info(Request $request)
    {
        return [
            'info' => $this->decrypt($request->input('enc')),
        ];
    }

    public function charge(Request $request)
    {
        $info = $this->decrypt($request->input('enc'));
        # lottery存在并且没有使用过
        $lottery = Lottery::where('uuid', $info->lottery)->first();
        if (empty($lottery) || $lottery->used) {
            throw new Exception('invalid lottery');
        }
        # user入参和lottery匹配
        if ($info->user !== $request->input('user')) {
            throw new Exception('invalid user');
        }
        # user必须真实存在
        $user = User::where('uuid', $info->user)->first();
        if (empty($user)) {
            throw new Exception('invalid user');
        }
        # lottery必须存在
        $cnt = Lottery::where('id', $lottery->id)->where('used', false)
            ->update(['used' => 1]);
        if ($cnt === 0) {
            throw new Exception('unknown error');
        }

        $user->coin += $lottery->coin;
        $user->save();

        return [
            // 'user' => $user,
            // 'lottery' => $lottery,
        ];
    }

    private function decrypt($enc)
    {
        $serilized = trim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, env('LOTTERY_KEY'), base64_decode($enc), MCRYPT_MODE_ECB));
        $info = json_decode($serilized);
        if (empty($info)) {
            throw new Exception('invalid lottery');
        }
        return $info;
    }
}
