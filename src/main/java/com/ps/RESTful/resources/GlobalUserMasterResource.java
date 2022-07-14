package com.ps.RESTful.resources;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ps.RESTful.dto.request.GlobalUserMasterRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface GlobalUserMasterResource {

	public static final String RESOURCE_PATH = "/users";

	/*
	 * Before It was like:- public static final String RESOURCE_PATH_WITH_COMPANY =
	 * "/company/{companyId}/users";
	 */
	public static final String RESOURCE_PATH_WITH_COMPANY = "/";

	public static final String BATCH_REQUEST_PATH = "/batch/company/{companyId}";
	public static final String VERIFY_REQUEST_PATH = "/verify";
	public static final String PASSWORD_REQUEST_PATH = "{resourceId}/password";
	public static final String LOGIN_REQUEST_PATH = "/login";
	public static final String GETBYID = "/find/{resourceId}";
	public static final String GETBYCompanyId = "/find/company/{resourceId}";
	public static final String REFRESH_JWT_TOKEN = "/refresh/token";

	/*
	 * Before there was no path mentioned: -Added by MayurG
	 */
	@PostMapping(path = RESOURCE_PATH_WITH_COMPANY, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> add(@RequestBody GlobalUserMasterRequestDTO requestDTO);

	@PostMapping(path = BATCH_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addAll(@PathVariable("companyId") int companyId,
			@RequestBody List<GlobalUserMasterRequestDTO> requestDTOList);

	@PostMapping(path = VERIFY_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> verify(@RequestBody GlobalUserMasterRequestDTO requestDTO,
			@RequestParam(name = "type", required = true) String type);

	@PostMapping(path = PASSWORD_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> setResetPassword(@PathVariable("resourceId") int resourceId,
			@RequestBody GlobalUserMasterRequestDTO requestDTO,
			@RequestParam(name = "action", required = true) String action);

	@PostMapping(path = LOGIN_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> login(@RequestBody GlobalUserMasterRequestDTO requestDTO);

	/*
	 * Added-By-MayurG-will-be-used-by-other-services-to-know-the-login-userDetails
	 */
	@GetMapping(path = GETBYID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getById(@PathVariable("resourceId") int resourceId);

	/*
	 * Added-By-MayurG-will-be-used-to-get-all-user-details-by-companyId
	 */
	@GetMapping(path = GETBYCompanyId, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getByCompanyId(@PathVariable("resourceId") int resourceId);

	/*
	 * Re-Generate-JWT-Token
	 * This-request-will-fetch-userDetails-from-passed-JWT-Token-(It-Must-be-Active/
	 * Valid/Yet-toExpire-and-then-Re-Generate-the-token-using-the-information-available-in-passed-JWT
	 */
	@GetMapping(path = REFRESH_JWT_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> reGenerateJWTToken();

}
