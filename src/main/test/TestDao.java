import com.yue.Crawel.Service.DoubanService;
import com.yue.Crawel.dao.DoubanCommetMapper;
import com.yue.Crawel.dao.UserMapper;
import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.model.MovieDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Andrew on 16/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-servlet.xml"})
public class TestDao {

    @Resource
    private DoubanCommetMapper doubanCommetMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DoubanService doubanService;

    @Test
    public void testDao(){
        DoubanComment doubanComment = new DoubanComment();
        doubanComment.setMovieName("好电影");
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setMovieName("澳门风云");
        movieDetail.setActors("fda");
        movieDetail.setMovieImg("fasfsdfas");
        movieDetail.setType("fdsf");
        movieDetail.setTime("2015-05-05");

//        doubanCommetMapper.saveMovieDetail(movieDetail);
//        doubanService.save(doubanComment);
//        List<DoubanComment> testList = doubanCommetMapper.getDoubanCommentsFromDB("澳门风云");
//        System.out.println(testList.get(0));
        System.out.println(doubanCommetMapper.getBackUpMovieDetail("澳门风云"));
//        System.out.println(doubanCommetMapper.getMovieDetailFromDB("澳门风云").getActors());
//        List<DoubanComment> doubanComments= new ArrayList<DoubanComment>();
//        doubanComments.add(doubanComment);
//        doubanService.saveComments2DB(doubanComments);
    }
}
