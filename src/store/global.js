// 本文件定义了一些全局js变量和函数
import defaultUserIcon from "../assets/defaultUserIcon";

// 用户数据，如果未登录则显示默认头像
let user = {
  username: "",
  userIcon: defaultUserIcon
};

let isMobile = false; // 是否为手机端
let isAdministrator = false;
let isLogin = false;
let courseId = -1;
let sectionId = -1;
let fileId = -1; // 预览文件的id
let path = "/"; // 当前路径

export {
  user,
  isMobile,
  isAdministrator,
  isLogin,
  courseId,
  sectionId,
  fileId,
  path
};
