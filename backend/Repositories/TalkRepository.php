<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:00
 */

namespace Repositories;

use Utilities\Utitilies;

class TalkRepository{

    /**
     * @return array
     */
    public static function getTalks(){
        $sql_query = "SELECT id, title, `from`, `to`, content, room_id, IF(isKeynote = 1,'true','false') as isKeynote FROM talk;";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function getTalksWithFavorites($userid){
        $sql_query = "SELECT *, IF((SELECT COUNT(*) FROM user_talk WHERE user_id = :userid)>0,'true','false') as isFavorite FROM talk;";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        $stmt->execute(array(':userid'=>$userid));
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function getTalkById($id)
    {
        $sql_query = "SELECT * FROM talk WHERE id=:id;";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    public static function insertTalk($id, $roomId, $isKeynote, $title, $from, $to, $content)
    {
        $sql_query = "INSERT INTO talk (id,title,`from`,`to`,content,room_id,isKeynote) VALUES (:id,:title,:from,:to,:content,:room_id,:isKeynote);";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        $stmt->bindParam(":isKeynote",$isKeynote,\PDO::PARAM_INT);
        $stmt->bindParam(":title",$title);
        $stmt->bindParam(":from",$from);
        $stmt->bindParam(":to",$to);
        $stmt->bindParam(":content",$content);
        $stmt->bindParam(":room_id",$roomId);
        $stmt->bindParam(":id",$id);
        //return $stmt->execute(array(':id'=>$id,':title'=>$title,':from'=>$from,':to'=>$to,':content'=>$content,':room_id'=>$roomId,':isKeynote'=>$isKeynote));
        $stmt->execute();
    }

    public static function updateTalk($id, $roomId, $isKeynote, $title, $from, $to, $content)
    {
        $sql_query = "UPDATE talk SET title=:title,`from`=:from,`to`=:to,content=:content,room_id=:room_id,isKeynote=:isKeynote WHERE id=:id;";
        $con = Utitilies::getConnection();
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