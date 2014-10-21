<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:38
 */

namespace Repositories;

use Utilities\Utitilies;

class UserRepository{
    /**
     * @param $id
     * @return bool
     */
    public static function insertUser($id){
        $sql_query = "INSERT INTO user VALUES (:id);";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id));
    }

    public static function insertFavorite($userid,$talkid){
        $sql_query = "INSERT INTO user_talk VALUES (:userid, :talkid);";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':userid'=>$userid,':talkid'=>$talkid));
    }
}