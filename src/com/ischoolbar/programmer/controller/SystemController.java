package com.ischoolbar.programmer.controller;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.github.pagehelper.util.StringUtil;
import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.service.UserService;
import com.ischoolbar.programmer.util.CpachaUtil;
import org.apache.commons.lang.StringUtils;

/*
 * 系统控制器
 */
@RequestMapping("/system")
@Controller
public class SystemController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.setViewName("hello");
		return model;
	}
	/*
	 * 登录页面
	 */
	@RequestMapping(value="/login",method = RequestMethod.GET )
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login");
		return model;
	}
	/*
	 * 登录表单提交
	 */
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> login(
			@RequestParam(value="password",required=true)String password,
			@RequestParam(value="username",required=true)String username,
			@RequestParam(value="vcode",required=true)String vcode,
			@RequestParam(value="type",required=true)int type,
			HttpServletRequest request
			) {
		Map<String,String> ret=new HashMap<String,String>();
		if(StringUtil.isEmpty(username)) {
			ret.put("type","error");
			ret.put("msg","用户名不能为空");
			return ret;
		}
		if(StringUtil.isEmpty(password)) {
			ret.put("type","error");
			ret.put("msg","密码不能为空");
			return ret;
		}
		if(StringUtil.isEmpty(vcode)) {
			ret.put("type","error");
			ret.put("msg","验证码不能为空");
			return ret;
		}
		String loginCpachar=(String)request.getSession().getAttribute("loginCpacha");
		if(StringUtils.isEmpty(loginCpachar)) {
			ret.put("type","error");
			ret.put("msg","长时间未操作，绘会话已失效请刷新后重试！");
			return ret;
		}
		if(!vcode.toUpperCase().equals(loginCpachar.toUpperCase())) {
			ret.put("type","error");
			ret.put("msg","验证码错误");
			return ret;
		}
		request.getSession().setAttribute(loginCpachar, null);
		//从数据库中查找
		
		User user = userService.findByUserName(username);
		if(user==null) {
			ret.put("type", "error");
			ret.put("msg","不存在该用户");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg","登录成功");
		return ret;
	}
	
	/*
	 * 验证码
	 */
	@RequestMapping(value="get_cpacha",method=RequestMethod.GET)
	public void getCpacha(HttpServletRequest request,
			@RequestParam(value="v1",defaultValue = "4",required = false)Integer vl,
			@RequestParam(value="w",defaultValue = "98",required = false)Integer w,	
			@RequestParam(value="h",defaultValue = "33",required = false)Integer h,	
			
			HttpServletResponse response) {
		CpachaUtil cpachaUtil =new CpachaUtil(vl,w,h);
		String generatorVCode = cpachaUtil.generatorVCode();
		request.getSession().setAttribute("loginCpacha", generatorVCode);
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorRotateVCodeImage,"gif",response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	


}
