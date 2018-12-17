
-- 资源表
create table permission(
	id int(11),
	name varchar(25) COMMENT '资源名称',
	url varchar(25) COMMENT '资源url',
	tag varchar(25) COMMENT '资源标识',
	permission_desc varchar(50) COMMENT '提示信息',
	PRIMARY KEY (id)
)

-- 角色表
create table role(
	id int(11),
	name varchar(25) COMMENT '角色名称',
	role_desc varchar(25) COMMENT '角色说明',
	PRIMARY KEY (id)
)

-- 角色资源表
create table role_permission(
  r_id int(11),
  p_id int(11)
)

-- 用户表
create table user(
	id int(11),
	username varchar(25) COMMENT '账号',
	realname varchar(25) COMMENT '真实姓名',
	password varchar(25) COMMENT '登录密码',
	create_date datetime COMMENT '创建日期',
	last_login_time datetime COMMENT '最后登录日期',
	enabled tinyint(1) COMMENT '是否可用',
	account_non_expired tinyint(1) COMMENT '是否过期',
	account_non_locked tinyint(1) COMMENT '是否锁定',
	credentials_non_expired tinyint(1) COMMENT '用户证书是否有效',
	PRIMARY KEY (id)
);

-- 用户角色表
create table user_role(
	u_id int(11),
	r_id int(11)
);





