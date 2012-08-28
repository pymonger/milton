
package scratch;

import com.bradmcevoy.http.Resource;
import com.ettrema.http.caldav.demo.TResourceFactory;

/**
 *
 * @author alex
 */
public class TResourceFactoryFindTest {
  public static void main(String[] args)
  {
    TResourceFactory factory = new TResourceFactory();
    Resource resource = factory.getResource(null, "http://localhost:9080/calendarHome/calendarOne/");
    System.out.println("FOUND : "+resource);
  }
}
