package vggames.git

import scala.collection.JavaConverters._
import vggames.shared.task.Task
import java.util.{ List => JUList }
import scala.collection.mutable.ListBuffer

case class Git(parent : Git, command : Command, commits : Map[String, List[Commit]], branch : String) {

  def ~(command : Command) = command(this, this)

  def ~<(command : Command) = command(this, parent)

  def getCommits : JUList[CommitList] = findCommits.asJava

  def findCommits : List[CommitList] = (List(br("stash"), br("work")) ++ nonSpecial ++
    List(br("master"), br("origin/master")) ++ nonSpecialRemotes).flatten

  def getBranch = branch

  def br(branch : String) = commits.get(branch).map(CommitList(branch, _))

  private def nonSpecial = commits.map(t => CommitList(t._1, t._2)).
    filterNot(e => Set("stash", "work", "master").contains(e.branch) || e.branch.contains("/")).
    map(Option(_))

  private def nonSpecialRemotes = commits.map(t => CommitList(t._1, t._2)).
    filter(e => e.branch.contains("/") && e.branch != "origin/master").
    map(Option(_))

  def tasks : List[GitTask] = {
    val gits = allGits(this).reverse
    gits.zip(gits.tail).map(t => GitTask(t._1, t._2))
  }

  private def allGits(repo : Git) : List[Git] = {
    if (repo == null) List()
    else repo :: allGits(repo.parent)
  }

  def diff(expected : Git) : List[String] = {
    val buffer = ListBuffer[String]()
    if (branch != expected.branch)
      buffer += "Deveria mudar para o branch <code>%s</code>. Est&aacute; em <code>%s</code>".format(expected.branch, branch)

    expected.commits.keySet.diff(commits.keySet).foreach { branch =>
      buffer += "Deveria criar o branch <code>%s</code>.".format(branch)
    }

    commits.keySet.diff(expected.commits.keySet).foreach { branch =>
      buffer += "N&atilde;o deveria criar o branch <code>%s</code>.".format(branch)
    }

    expected.commits.keySet.intersect(commits.keySet).foreach { branch =>
      expected.commits(branch).zip(commits(branch)).foldLeft(0) { (i, commit) =>
        if (commit._1 != commit._2)
          buffer += "Commit <code>%s</code> do branch <code>%s</code> deveria ser <code>%s</code>, mas foi <code>%s</code>".
            format(i, branch, commit._1, commit._2)
        i + 1
      }
    }

    buffer.toList
  }

}

case class CommitList(branch : String, commits : List[Commit]) {
  def getBranch = branch
  def getCommits = commits.asJava
}

object EmptyGit {
  def apply() = new Git(null, null, Map("work" -> List()), "work")
}
