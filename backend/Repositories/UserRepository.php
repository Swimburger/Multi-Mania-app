<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:38
 */

namespace Repositories;

use Utilities\Utilities;

class UserRepository{
    /**
     * @param $id
     * @return bool
     */
    public static function insertUser($id){
        $sql_query = "INSERT INTO user VALUES (:id,NOW());";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id));
    }

    public static function insertFavorite($userid,$talkid){
        $sql_query = "INSERT INTO user_talk VALUES (:userid, :talkid);
                      UPDATE user SET last_updated=NOW() WHERE id=:userid2;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':userid'=>$userid,':talkid'=>$talkid,':userid2'=>$userid));
    }

    public static function removeFavorite($userid,$talkid){
        $sql_query = "DELETE FROM user_talk WHERE user_id=:userid AND talk_id=:talkid;
                      UPDATE user SET last_updated=NOW() WHERE id=:userid2;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':userid'=>$userid,':talkid'=>$talkid,':userid2'=>$userid));
    }

    /**
     * @param $id
     * @return user|null
     * Returns a user if there is a user in the db
     */
    public static function getUserById($id)
    {
        $sql_query = "SELECT * FROM user WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    public static function getLastUpdateByUser($id)
    {
        $sql_query = "SELECT last_updated FROM user WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }
}