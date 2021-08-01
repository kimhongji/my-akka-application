class MyAkkaHttp {
  val test = "now"
  println(test)
}

object MyAkkaHttp {
  def main(args: Array[String]): Unit ={
    println("start main")
    val start = new MyAkkaHttp()
    println("stop main")
  }
}