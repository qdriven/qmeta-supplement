package io.qmeta.supplement;

import cn.hutool.json.JSONUtil;
import lombok.Builder;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeanUtilsTest {

  @Data
  @Builder
  public static class User {
    private String name;
    private Integer age;
    private String location;
  }

  @Data
  public static class UserDTO {
    private String name;
    private Integer age;
  }

  @Data
  @Builder
  public static class GitRepoCreateReqVO {
    private Long templateId;
    private String repoName;
    private String repoDesc;
    // @ApiModelProperty(value = "项目编号", required = true)
    // @NotBlank
    // @Length(max = 32)
    private String projectNo;
    private String creator;
    @Builder.Default private boolean withJenkinsJob = false;
  }

  @Test
  public void testTransfer() {
    User user = User.builder().age(10).name("name").location("location").build();
    UserDTO userDTO = BeanUtils.toBean(user, UserDTO.class);
    Assertions.assertThat(userDTO.getAge()).isEqualTo(10);
    Assertions.assertThat(userDTO.getName()).isEqualTo("name");

    GitRepoCreateReqVO gitRepo = GitRepoCreateReqVO.builder().repoDesc("test").build();
    String json = JSONUtil.toJsonStr(gitRepo);
    System.out.println(json);
    GitRepoCreateReqVO repo1 =
        JSONUtil.toBean("{\"enableJenkinsJob\":1,\"repoDesc\":\"test\"}", GitRepoCreateReqVO.class);
    System.out.println(repo1);
  }
}
