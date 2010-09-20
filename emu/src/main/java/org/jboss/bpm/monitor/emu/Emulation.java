/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.bpm.monitor.emu;

import org.hibernate.Session;
import org.jboss.bpm.monitor.model.hibernate.HibernateUtil;
import org.jboss.bpm.monitor.model.bpaf.BPAFContext;
import org.jboss.bpm.monitor.model.bpaf.Event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.*;

public class Emulation implements ProcessEmulation, Runnable
{
  final static int POOL_SIZE = 1;

  private final ThreadPoolExecutor pool;
  private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(POOL_SIZE);

  private Thread daemon = null;
  private boolean shutdown = false;
  private boolean suspend = false;
  private EventHandler eventHandler;

  private static Class[] processDefinitions = new Class[] {
      OrderCheckoutProcess.class, RefillStockProcess.class
  };

  public Emulation(EventHandler handler)
  {
    this.eventHandler = handler;
    pool = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 20000, TimeUnit.MILLISECONDS, workQueue);
  }

  public void run()
  {
    while(!shutdown)
    {
      try
      {
        System.out.println("> Pool active: "+ pool.getActiveCount());
        System.out.println("> Pool capacity: "+ workQueue.remainingCapacity());

        if(!suspend)
        {


          Process p = Math.random()<0.8 ? new OrderCheckoutProcess() : new RefillStockProcess();

          ProcessRunnable process = new ProcessRunnable(p, eventHandler);
          try
          {
            pool.execute(process);
            System.out.println("\tStart new process: "+ p.getClass().getSimpleName());

          }
          catch (RejectedExecutionException e)
          {
            System.out.println("\tWaiting process termination");
          }
        }

        Thread.sleep(1000);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        shutdown = true;
      }
    }
  }

  public void suspend()
  {
    suspend = true;
  }

  public void start()
  {

    if(null==daemon) // basically lazy init
    {
      shutdown = false;
      daemon = new Thread(this);
      daemon.start();
    }
    else
    {
      suspend = false;
    }
  }

  public void shutdown()
  {
    shutdown = true;
    pool.shutdown();
  }


  public static void main(String[] args) throws Exception
  {
    if(args.length==0)
      throw new IllegalArgumentException("Usage: Emulation <jdbc|file>");

    if(args[0].equals("file"))
    {
      // ======= FILE  =========

      final JAXBContext jaxb = BPAFContext.newInstance();
      final Marshaller m = jaxb.createMarshaller();
      m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

      File file = new File(System.getProperty("user.home") + "/bpaf_data.txt");
      if(file.exists()) file.delete();


      final OutputStream out = new FileOutputStream(file);

      Emulation emu = new Emulation(new EventHandler()
      {
        public void processEvent(Event bpafEvent)
        {
          try
          {
            m.marshal(bpafEvent, out);
            out.write("\n".getBytes());
          }
          catch (Exception e)
          {
            throw new RuntimeException(e);
          }
        }
      });

      Runtime.getRuntime().addShutdownHook(
          new Thread(
              new Runnable()
              {
                public void run()
                {
                  try
                  {
                    out.close();
                  }
                  catch (IOException e)
                  {
                    e.printStackTrace();
                  }
                }
              }
          )
      );

      emu.start();
    }
    else
    {
      // ======= JDBC  =========
      final Session session = HibernateUtil.getSession();

      Emulation emu = new Emulation(new EventHandler()
      {
        public void processEvent(Event bpafEvent)
        {
          try
          {
            session.save(bpafEvent);
          }
          catch (Exception e)
          {
            throw new RuntimeException(e);
          }
        }
      });

      Runtime.getRuntime().addShutdownHook(
          new Thread(
              new Runnable()
              {
                public void run()
                {
                  try
                  {
                    session.close();
                  }
                  catch (Exception e)
                  {
                    e.printStackTrace();
                  }
                }
              }
          )
      );

      emu.start();
    }

  }
}
