<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:00
 */

namespace Repositories;

use Utilities\Utilities;

/**
 * The repository contains all methods for interacting with the database for the Talk model
 *
 * Class TalkRepository
 * @package Repositories
 */
class TalkRepository{

    /**
     * @return array Returns all talks
     */
    public static function getTalks(){
        $sql_query = "SELECT id, title, `from`, `to`, content, room_id, IF(isKeynote = 1,'true','false') as isKeynote FROM talk;";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $userid string The id of the user
     * @return array Returns all talks with an extra data member, isFavorite
     */
    public static function getTalksWithFavorites($userid){
        $sql_query = "SELECT id, title, `from`, `to`, content, room_id, IF(isKeynote = 1,'true','false') as isKeynote, IF((SELECT COUNT(*) FROM user_talk WHERE user_id = :userid)>0,'true','false') as isFavorite FROM talk;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        $stmt->execute(array(':userid'=>$userid));
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $id int The id of the talk
     * @return mixed|null Returns the talk if found, else null
     */
    public static function getTalkById($id)
    {
        $sql_query = "SELECT id, title, `from`, `to`, content, room_id, IF(isKeynote = 1,'true','false') as isKeynote FROM talk WHERE id=:id;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    /**
     * @param $id int The id of the talk
     * @param $roomId int The id of the room of the talk
     * @param $isKeynote int 0 or 1 to indicate if favorite (0=false,1=true)
     * @param $title string The title of the talk
     * @param $from string The start datetime of the talk
     * @param $to string The end datetime of the talk
     * @param $content string The content of the talk
     * @return bool Returns true if successful
     */
    public static function insertTalk($id, $roomId, $isKeynote, $title, $from, $to, $content)
    {
        $sql_query = "INSERT INTO talk (id,title,`from`,`to`,content,room_id,isKeynote) VALUES (:id,:title,:from,:to,:content,:room_id,:isKeynote);";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        $stmt->bindParam(":isKeynote",$isKeynote,\PDO::PARAM_INT);
        $stmt->bindParam(":title",$title);
        $stmt->bindParam(":from",$from);
        $stmt->bindParam(":to",$to);
        $stmt->bindParam(":content",$content);
        $stmt->bindParam(":room_id",$roomId);
        $stmt->bindParam(":id",$id);
        //return $stmt->execute(array(':id'=>$id,':title'=>$title,':from'=>$from,':to'=>$to,':content'=>$content,':room_id'=>$roomId,':isKeynote'=>$isKeynote));
        return $stmt->execute();
    }

    /**
     * @param $id int The id of the talk
     * @param $roomId int The id of the room of the talk
     * @param $isKeynote int 0 or 1 to indicate if favorite (0=false,1=true)
     * @param $title string The title of the talk
     * @param $from string The start datetime of the talk
     * @param $to string The end datetime of the talk
     * @param $content string The content of the talk
     * @return bool Returns true if successful
     */
    public static function updateTalk($id, $roomId, $isKeynote, $title, $from, $to, $content)
    {
        $sql_query = "UPDATE talk SET title=:title,`from`=:from,`to`=:to,content=:content,room_id=:room_id,isKeynote=:isKeynote WHERE id=:id;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        $stmt->bindParam(":isKeynote",$isKeynote,\PDO::PARAM_INT);
        $stmt->bindParam(":title",$title);
        $stmt->bindParam(":from",$from);
        $stmt->bindParam(":to",$to);
        $stmt->bindParam(":content",$content);
        $stmt->bindParam(":room_id",$roomId);
        $stmt->bindParam(":id",$id);
        //return $stmt->execute(array(':title'=>$title,':from'=>$from,':to'=>$to,':content'=>$content,':room_id'=>$roomId,':isKeynote'=>$isKeynote,':id'=>$id));
        return $stmt->execute();
    }


}