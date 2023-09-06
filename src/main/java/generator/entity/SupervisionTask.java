package generator.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
* 督导_课程关系表
* @TableName supervision_task
*/
public class SupervisionTask implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long id;
    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long courseid;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long userid;

    /**
    * 
    */
    private void setId(Long id){
    this.id = id;
    }

    /**
    * 
    */
    private void setCourseid(Long courseid){
    this.courseid = courseid;
    }

    /**
    * 
    */
    private void setUserid(Long userid){
    this.userid = userid;
    }


    /**
    * 
    */
    private Long getId(){
    return this.id;
    }

    /**
    * 
    */
    private Long getCourseid(){
    return this.courseid;
    }

    /**
    * 
    */
    private Long getUserid(){
    return this.userid;
    }

}
