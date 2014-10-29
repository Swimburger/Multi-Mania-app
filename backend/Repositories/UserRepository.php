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

    public static function removeFavorite($userid,$talkid){
        $sql_query = "DELETE FROM user_talk WHERE user_id=:userid AND talk_id=:talkid";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':userid'=>$userid,':talkid'=>$talkid));
    }

    /**
     * @param $id
     * @return user|null
     * Returns a user if there is a user in the db
     * Untested...
     */
    public static function getUserById($id)
    {
        $sql_query = "SELECT * FROM user WHERE id=:id;";
        $con=Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }
}