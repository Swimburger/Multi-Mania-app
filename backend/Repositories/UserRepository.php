<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:38
 */

namespace Repositories;

use Utilities\Utilities;

/**
 * The repository contains all methods for interacting with the database for the User model
 *
 * Class UserRepository
 * @package Repositories
 */
class UserRepository{
    /**
     * @param $id string The id of the user
     * @return bool Returns true if successful
     */
    public static function insertUser($id){
        $sql_query = "INSERT INTO user VALUES (:id,NOW());";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id));
    }

    /**
     * Favorites the talk for the user
     *
     * @param $userid string The userid of the user favoriting the talk
     * @param $talkid int The talkid of the talk to favorite
     * @return bool Returns true if successful
     */
    public static function insertFavorite($userid,$talkid){
        $sql_query = "INSERT INTO user_talk VALUES (:userid, :talkid);
                      UPDATE user SET last_updated=NOW() WHERE id=:userid2;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':userid'=>$userid,':talkid'=>$talkid,':userid2'=>$userid));
    }

    /**
     * Removes the favorite talk from the user
     *
     * @param $userid string The userid of the user favoriting the talk
     * @param $talkid int The talkid of the talk to favorite
     * @return bool Returns true if successful
     */
    public static function removeFavorite($userid,$talkid){
        $sql_query = "DELETE FROM user_talk WHERE user_id=:userid AND talk_id=:talkid;
                      UPDATE user SET last_updated=NOW() WHERE id=:userid2;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':userid'=>$userid,':talkid'=>$talkid,':userid2'=>$userid));
    }

    /**
     * @param $id string The id of the user
     * @return mixed|null Returns
     * Returns a user if found, else null
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

    /**
     * @param $id string
     * @return mixed|null Returns the date of the last update that user did (favorite)
     */
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