#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <time.h>
#include <math.h>

#define NX  149     /* X축 격자점 수 */
#define NY  253     /* Y축 격자점 수 */

struct lamc_parameter {
    float  Re;          /* 사용할 지구반경 [ km ]      */
    float  grid;        /* 격자간격        [ km ]      */
    float  slat1;       /* 표준위도        [degree]    */
    float  slat2;       /* 표준위도        [degree]    */
    float  olon;        /* 기준점의 경도   [degree]    */
    float  olat;        /* 기준점의 위도   [degree]    */
    float  xo;          /* 기준점의 X좌표  [격자거리]  */
    float  yo;          /* 기준점의 Y좌표  [격자거리]  */
    int    first;       /* 시작여부 (0 = 시작)         */
};

/******************************************************************************
 *
 *  MAIN
 *
 ******************************************************************************/
int main (int argc, char *argv[])
{
    
    float  lon, lat, x, y;
    struct lamc_parameter map;
    
    if (argc != 3)
    {
        printf("[Usage] %s <longitude> <latitude>\n", argv[0]);   
        exit(0);
    }

    lon = atof(argv[1]);
    lat = atof(argv[2]);

    map.Re    = 6371.00877;     // 지도반경
    map.grid  = 5.0;            // 격자간격 (km)
    map.slat1 = 30.0;           // 표준위도 1
    map.slat2 = 60.0;           // 표준위도 2
    map.olon  = 126.0;          // 기준점 경도
    map.olat  = 38.0;           // 기준점 위도
    map.xo    = 210/map.grid;   // 기준점 X좌표
    map.yo    = 675/map.grid;   // 기준점 Y좌표
    map.first = 0;
 
    float x1, y1;
    static double  PI, DEGRAD, RADDEG;
    static double  re, olon, olat, sn, sf, ro;
    double         slat1, slat2, alon, alat, xn, yn, ra, theta;

    if (map.first == 0) {
        PI = asin(1.0)*2.0;
        DEGRAD = PI/180.0;
        RADDEG = 180.0/PI;

        re = map.Re/map.grid;
        slat1 = map.slat1 * DEGRAD;
        slat2 = map.slat2 * DEGRAD;
        olon = map.olon * DEGRAD;
        olat = map.olat * DEGRAD;

        sn = tan(PI*0.25 + slat2*0.5)/tan(PI*0.25 + slat1*0.5);
        sn = log(cos(slat1)/cos(slat2))/log(sn);
        sf = tan(PI*0.25 + slat1*0.5);
        sf = pow(sf,sn)*cos(slat1)/sn;
        ro = tan(PI*0.25 + olat*0.5);
        ro = re*sf/pow(ro,sn);
        map.first = 1;
    }

    ra = tan(PI*0.25+(lat)*DEGRAD*0.5);
    ra = re*sf/pow(ra,sn);
    theta = (lon)*DEGRAD - olon;
    if (theta >  PI) theta -= 2.0*PI;
    if (theta < -PI) theta += 2.0*PI;
    theta *= sn;
    x = (int)((float)(ra*sin(theta)) + map.xo + 1.5);
    y = (int)((float)(ro - ra*cos(theta)) + map.yo + 1.5);

    printf("lon.= %f, lat.= %f ---> X = %d, Y = %d\n", lon, lat, (int)x, (int)y);

    return 0;
}
