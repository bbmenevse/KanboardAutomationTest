package api.projectapi;

import api.APIHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
/**
 * The reason behind taking userName and password as inputs is,
 * the api calls will have different responses to different users/roles.
 */

/*
A single method could have sufficed if needed. It would take userName, userPassword, api method, parameters.
That would be better in my opinion but for this project, I will create CRUD methods.
 */
    /*
    Admin account can't update a project that was created using API, as owner is not set if not specified.
    Even admin accounts can't update a project via API, even though they can do it on browser.
    Just encountered same problem with removeProject. You can't delete a project if you are not assigned to the project,
    even as admin.
     */
public class ProjectAPI {

    private final String userName;
    private final String password;

    // Constructor to initialize username and password
    public ProjectAPI(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public int getNumberOfProjects(){
        // Set the base URI

        String requestBody = APIHelper.buildRequest("getAllProjects");

        // Make the API call with Basic Auth
        Response response = APIHelper.sendRequest(requestBody,userName,password);

        JsonPath jsonPath = response.jsonPath();

        return jsonPath.getList("result").size();
    }

    /*
    Kanboard returns an empty body in contrast to what is written on:
    https://docs.kanboard.org/v1/api/project_procedures/#createproject
    If, the json does not have user id.
     */
    public Response createProject(Map<String, Object> params) {
        String requestBody = APIHelper.buildRequest("createProject",params);
        Response response = APIHelper.sendRequest(requestBody,userName,password);
        return response;
    }

    public Response getProjectById(Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("project_id",id);
        String requestBody = APIHelper.buildRequest("getProjectById",params);
        Response response = APIHelper.sendRequest(requestBody,userName,password);
        return response;
    }

    public Response getProjectByName(String projectName){
        Map<String, Object> params = new HashMap<>();
        params.put("name",projectName);
        String requestBody = APIHelper.buildRequest("getProjectByName",params);
        Response response = APIHelper.sendRequest(requestBody,userName,password);
        return response;
    }

    public Response getProjectByIdentifier(String identifier){
        Map<String, Object> params = new HashMap<>();
        params.put("identifier",identifier);
            String requestBody = APIHelper.buildRequest("getProjectByIdentifier",params);
        Response response = APIHelper.sendRequest(requestBody,userName,password);
        return response;
    }

    // There is no update by name or identifier method, only id.
    // If a user does not have permission on a project, they will not be able to update it, even if admin.
    public Response updateProjectById(Map<String, Object> params){

        String requestBody = APIHelper.buildRequest("updateProject",params);
        Response response = APIHelper.sendRequest(requestBody,userName,password);
        return response;

        //ApiResponseValidator.validateResponse(response,200);

    }

    public Response deleteProject(int projectId){

        Map<String, Object> params = new HashMap<>();
        params.put("project_id",projectId);

        String requestBody = APIHelper.buildRequest("removeProject",params);
        Response response = APIHelper.sendRequest(requestBody,userName,password);
        return response;

        //ApiResponseValidator.validateResponse(response,200);
    }


}