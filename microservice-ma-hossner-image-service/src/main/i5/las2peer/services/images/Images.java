package i5.las2peer.services.images;


import java.net.HttpURLConnection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import i5.las2peer.api.Context;
import i5.las2peer.api.ManualDeployment;
import i5.las2peer.api.ServiceException;
import i5.las2peer.api.logging.MonitoringEvent;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.services.images.database.DatabaseManager;
import java.sql.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import org.json.simple.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List; 
import java.util.ArrayList; 
import java.util.concurrent.TimeUnit;
 

/**
 *
 * ma-hossner-image-service
 *
 * This microservice was generated by the CAE (Community Application Editor). If you edit it, please
 * make sure to keep the general structure of the file and only add the body of the methods provided
 * in this main file. Private methods are also allowed, but any "deeper" functionality should be
 * outsourced to (imported) classes.
 *
 */
@ServicePath("images")
@ManualDeployment
public class Images extends RESTService {


  /*
   * Database configuration
   */
  private String jdbcDriverClassName;
  private String jdbcLogin;
  private String jdbcPass;
  private String jdbcUrl;
  private static DatabaseManager dbm;



  public Images() {
	super();
    // read and set properties values
    setFieldValues();
        // instantiate a database manager to handle database connection pooling and credentials
    dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl);
  }

  @Override
  public void initResources() {
	getResourceConfig().register(RootResource.class);
  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // REST methods
  // //////////////////////////////////////////////////////////////////////////////////////

  @Api
  @SwaggerDefinition(
      info = @Info(title = "ma-hossner-image-service", version = "3",
          description = "Simple image hosting service.",
          termsOfService = "",
          contact = @Contact(name = "Philipp Hossner", email = "CAEAddress@gmail.com") ,
          license = @License(name = "BSD",
              url = "https://github.com/CAE-Community-Application-Editor/microservice-ma-hossner-image-service/blob/master/LICENSE.txt") ) )
  @Path("/")
  public static class RootResource {

    private final Images service = (Images) Context.getCurrent().getService();

      /**
   * 
   * addImage
   *
   * 
   * @param image  a JSONObject
   * 
   * @return Response 
   * 
   */
  @POST
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "uploadedImage")
  })
  @ApiOperation(value = "addImage", notes = " ")
  public Response addImage(String image) {
    JSONObject image_JSON = (JSONObject) JSONValue.parse(image);





    try {
      PreparedStatement preparedStmt = this.service.dbm.getConnection().prepareStatement("INSERT INTO Images (imageData) VALUES (?)");
      preparedStmt.setString(1, (String) image_JSON.get("image"));
      preparedStmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
    // service method invocations

     





    // uploadedImage
    boolean uploadedImage_condition = true;
    if(uploadedImage_condition) {
      JSONObject uploadedImageResults = new JSONObject();
      uploadedImageResults.put("msg", "upload successful");
            

      return Response.status(HttpURLConnection.HTTP_OK).entity(uploadedImageResults.toJSONString()).build();
    }
    return null;
  }

  /**
   * 
   * getImages
   *
   * 
   *
   * 
   * @return Response 
   * 
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "images")
  })
  @ApiOperation(value = "getImages", notes = " ")
  public Response getImages() {



    long processingStart = System.currentTimeMillis();

    ResultSet results;
    try {
      results = this.service.dbm.getConnection().createStatement().executeQuery("SELECT imageData FROM Images");
    } catch (SQLException e) {
      e.printStackTrace();
      return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).build();
    } 
    // service method invocations

     

    long processingFinished = System.currentTimeMillis();
    long processingDuration = processingFinished - processingStart;
    JSONObject processingDurationObj = new JSONObject();
    processingDurationObj.put("time", processingDuration);
    processingDurationObj.put("method", "GET");
    processingDurationObj.put("resource", "Images");
    Context.get().monitorEvent((Object)null, MonitoringEvent.SERVICE_CUSTOM_MESSAGE_1, processingDurationObj.toJSONString(), false);

    // images
    boolean images_condition = true;
    if(images_condition) {
      JSONObject imagesResult = new JSONObject();

      List<String> imageList = new ArrayList<>();
      try {
        while (results.next()) {
          imageList.add(results.getString(1));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      imagesResult.put("images", imageList);      

      return Response.status(HttpURLConnection.HTTP_OK).entity(imagesResult.toJSONString()).build();
    }
    return null;
  }



  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // Service methods (for inter service calls)
  // //////////////////////////////////////////////////////////////////////////////////////
  
  

  // //////////////////////////////////////////////////////////////////////////////////////
  // Custom monitoring message descriptions (can be called via RMI)
  // //////////////////////////////////////////////////////////////////////////////////////

  public Map<String, String> getCustomMessageDescriptions() {
    Map<String, String> descriptions = new HashMap<>();
        descriptions.put("SERVICE_CUSTOM_MESSAGE_1", "# HTTP Response Duration of Method getImages (GET)\n"
        + "\n"
        + "The number of milliseconds until the response is returned is logged according to the following JSON pattern:\n"
        + "```json\n"
        + "{ \"time\": <time_in_ms>, \"method\": <method_name>, \"resource\": <resource_name> }\n"
        + "```\n"
        + "## Example Measures\n"
        + "### Response Duration\n"
        + "Show in a line chart how long each request took to be processed.\n"
        + "```sql\n"
        + "SELECT TIME_STAMP, CAST(JSON_EXTRACT(REMARKS,\"$.time\") AS UNSIGNED) AS timing FROM MESSAGE WHERE EVENT=\"SERVICE_CUSTOM_MESSAGE_1\" AND SOURCE_AGENT IN $SERVICES$\n"
        + "```\n"
        + "Visualization: line chart\n"
        + "\n"
        + "## Number of times getImages (GET) took longer than 400ms\n"
        + "```sql\n"
        + "SELECT COUNT(*) FROM MESSAGE WHERE EVENT=\"SERVICE_CUSTOM_MESSAGE_1\" AND SOURCE_AGENT IN $SERVICES$ AND CAST(JSON_EXTRACT(REMARKS,\"$.time\") AS UNSIGNED) > 400\n"
        + "```\n"
        + "Visualization: line chart\n");

    return descriptions;
  }

}
