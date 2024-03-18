package org.cellocad.api;

import org.apache.tools.ant.DirectoryScanner;
import org.cellocad.authenticate.Authenticator;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.cellocad.api.BaseController;

import java.io.IOException;

@RestController
public class TestBaseController {
	public static void main(String[] args) {
	    BaseController base = new BaseController();
	
	    System.out.println("path = " + base._resultPath);
	}
}